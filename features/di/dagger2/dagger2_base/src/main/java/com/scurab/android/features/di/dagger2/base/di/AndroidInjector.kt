package com.scurab.android.features.di.dagger2.base.di

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.scurab.android.features.di.dagger2.base.BaseActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Help class to make an injection little bit easier and help to make it little bit more dynamic.
 *
 * To use this class there are following expectations about the environment:
 * - [Application] MUST implement [InternalActivityComponentProvider]
 *   take a look /app/src/main/java/com/scurab/android/samples/App.kt
 * - Every module has 2 key items a dagger component marked as [DIComponent] and a provider interface marked as [DIComponentProvider],
 *   take a look /app/src/main/java/com/scurab/android/features/di/dagger2/feature1/di/Feature1Component.kt
 * - [InternalActivityProvider] then extends all defined ComponentProviders,
 *   take a look /features/di/dagger2/dagger2_app/src/main/java/com/scurab/android/features/di/dagger2/app/Dagger2.kt
 */
@Suppress("UNCHECKED_CAST")
object AndroidInjector {

    /**
     * Get specific component for a fragment
     *
     * Usage in a fragment onCreate or onActivityCreated:
     * ```
     * AndroidInjector
     *      .component(this, Feature1Component.class)
     *      .inject(this)
     * ```
     *
     * This code expects:
     * Owning Activity implements [DIComponentHolder<Feature1Component>]
     */
    fun <T : DIComponent> component(fragment: Fragment, clazz: Class<T>): T {
        return component(fragment.requireActivity(), clazz)
    }

    private fun <T : DIComponent> component(activity: Activity, clazz: Class<T>): T {
        (activity as? DIComponentHolder<*>)?.let { activityAsHolder ->
            val activityComponent = activityAsHolder.component
            //try to check if Activity component is our expected component => in-feature-module DI
            if (clazz.isAssignableFrom(activityComponent.javaClass)) {
                return activityComponent.assertObjectCast(clazz)
            } else if (clazz != CommonComponent::class.java) {
                activityComponent.assertObjectCast(BaseActivity::class.java)

                //modular-app case
                //being here means that we might be injecting Fragment living in an activity
                //defined in another feature module =>
                //InternalActivityComponent needs to extend particular feature-module component
                //in this case
                val internalActivityComponent =
                    componentProvider(activity, InternalActivityComponentProvider::class.java)
                        .internalActivityComponent(activity as BaseActivity)

                return internalActivityComponent as? T
                    ?: throw IllegalStateException("InternalActivityComponent doesn't extend '${clazz.name}'")
            }
        }

        //all our production activities should implement DIComponentHolder
        //being here should be only in very special cases for example
        //QA (EnvironmentSelectorActivity), ShortcutTrampolineActivity
        if (clazz == CommonComponent::class.java) {
            return componentProvider(activity, CommonComponentProvider::class.java).commonComponent() as T
        }

        //that environment is not properly set up
        //you have to have either Activity implementing DIComponentHolder or
        //just using CommonComponent which is simple and useful only for basic stuff like views etc.
        throw IllegalArgumentException("${activity.javaClass.name} doesn't implement ${DIComponentHolder::class.java.name}")

    }

    /**
     * Get a component provider from the activity
     */
    private fun <T : DIComponentProvider> componentProvider(activity: BaseActivity, clazz: Class<T>): T {
        val componentProvider = (activity.application as? InternalActivityComponentProvider)
            ?: throw IllegalStateException("Application has to implement InternalActivityComponentProvider")

        //if someone is asking just for InternalActivityComponentProvider, just return directly
        if (clazz == InternalActivityComponentProvider::class.java) {
            return componentProvider as T
        }

        //AppComponent -> SessionComponent -> InternalActivityComponent -> FeatureModuleComponent
        //=> we can easily inject everywhere the BaseActivity instance
        val diComponent = componentProvider.internalActivityComponent(activity)

        return if (clazz.isAssignableFrom(diComponent.javaClass)) {
            diComponent.assertObjectCast(clazz)
        } else {
            componentProviderFallback(activity, clazz)
        }
    }

    private fun <T : DIComponentProvider> componentProvider(context: Context, clazz: Class<T>): T {
        val activity = context.asActivity()
        activity
            ?.let { it as? BaseActivity }
            ?.let {
                //just be nice and handle it as DoubleDispatch call
                return componentProvider(it, clazz)
            }

        (activity as AppCompatActivity?)?.let {
            val component = (context.applicationContext as InternalActivityComponentProvider)
                .internalActivityComponent(it)
            return component.assertObjectCast(clazz)
        }
        return componentProviderFallback(context, clazz)
    }

    //don't call this directly, this is when previous componentProvider failed
    private fun <T : DIComponentProvider> componentProviderFallback(context: Context, clazz: Class<T>): T {
        val provider = context.applicationContext as? DIComponentHolder<*>
        provider?.let { p ->
            p.component.assertObjectCast(clazz)
        }
        TODO("context is ${clazz.simpleName}, to get a SessionScope, App has to implement DIComponentHolder<SessionScope>")
    }

    /**
     * Get a dependencies (InternalActivityComponent has to implement the dependencies interface)
     */
    fun <T : DIComponentDependencies> dependencies(activity: BaseActivity): T {
        val internalActivityComponent = componentProvider(activity, InternalActivityComponentProvider::class.java)
            .internalActivityComponent(activity)
        return internalActivityComponent as T
    }

    private fun <T> Any.assertObjectCast(clazz: Class<T>): T {
        return this as? T
            ?: throw IllegalStateException("'${this.javaClass.name}' doesn't implement/extend '${clazz.name}'")
    }

    /**
     * Field delegate for the activity to preserve a component in the dynamic module activity
     *
     * Usage in an activity as a delegated field:
     * ```
     * val component by dagger(Feature1ComponentProvider::class.java, false) {
     *      feature1Component()
     * }
     * ```
     */
    fun <T : DIComponentProvider, R : DIComponent> AppCompatActivity.dagger(
        clazz: Class<out T>,
        survivingScope: Boolean = false,
        block: T.() -> R
    ): ReadOnlyProperty<Any, R> {
        return object : ReadOnlyProperty<Any, R> {
            private val component: R by lazy {
                val result = if (survivingScope) {
                    ViewModelProviders.of(this@dagger).get(DINode::class.java).run {
                        component = component ?: block(componentProvider(this@dagger, clazz))
                        component
                    }
                } else {
                    block(componentProvider(this@dagger, clazz))
                }
                result as R
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): R {
                return component
            }
        }
    }

    /**
     * Field delegate for the activity to preserve a component in the dynamic module activity
     *
     * Usage in an activity (in dynamic feature module) as a delegated field:
     * ```
     * val component: DynamicFeatureComponent by daggerDynamicModule(true) {
     *      DaggerDynamicFeatureComponent
     *          .builder()
     *          .dynamicFeatureComponentDependencies(AndroidInjector.dependencies(this))
     *          .build()
     * }
     * ```
     */
    fun <T : DIComponent> AppCompatActivity.daggerDynamicModule(
        survivingScope: Boolean = false,
        block: () -> T
    ): ReadOnlyProperty<Any, T> {
        return object : ReadOnlyProperty<Any, T> {
            private val component: T by lazy {
                val result = if (survivingScope) {
                    ViewModelProviders.of(this@daggerDynamicModule).get(DINode::class.java).run {
                        component = component ?: block()
                        component
                    }
                } else {
                    block()
                }
                result as T
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): T {
                return component
            }
        }
    }

    /**
     * Get an activity from a context
     */
    private fun Context.asActivity(): Activity? {
        var context = this
        var activity: Activity? = null
        while (context is ContextWrapper) {
            if (context is Activity) {
                activity = context
                break
            }
            context = context.baseContext
        }
        return activity
    }
}

internal class DINode : ViewModel() {
    var component: DIComponent? = null
}