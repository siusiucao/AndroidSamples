package com.scurab.android.features.di.dagger2.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

object AndroidInjector {

    fun <T : DIComponent> component(
        fragment: Fragment,
        clazz: Class<T>
    ): T {
        return component(fragment.requireActivity(), clazz)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : DIComponent> component(activity: Activity, clazz: Class<T>): T {

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
            return componentProvider(activity, CommonComponentProvider::class.java)
                .commonComponent() as T
        }

        //that environment is not properly set up
        //you have to have either Activity implementing DIComponentHolder or
        //just using CommonComponent which is simple and useful only for basic stuff like views etc.
        throw IllegalArgumentException("${activity.javaClass.name} doesn't implement ${DIComponentHolder::class.java.name}")

    }

    @Suppress("UNCHECKED_CAST")
    fun <T : DIComponentProvider> componentProvider(activity: BaseActivity, clazz: Class<T>): T {
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
            clazz.cast(diComponent) ?: throw IllegalArgumentException("Unable to cast ${diComponent.javaClass.name} to ${clazz.name}")
        } else {
            if(true) {
                //this means InternalActivityComponent is NOT your componentProvider (clazz)
                throw IllegalStateException("${diComponent.javaClass} doesn't extend ${diComponent.javaClass}")
            }

            //InternalActivityComponent doesn't extend from expected one, let's try to use
            //just SessionComponent, this is needed for example for QaComponentProvider
            val provider = activity.applicationContext as DIComponentHolder<*>
            clazz.cast(provider.component)
                ?: throw IllegalArgumentException("Unable to cast ${diComponent.javaClass.name} to ${clazz.name}")
        }
    }

    fun <T : DIComponentProvider> componentProvider(context: Context, clazz: Class<T>): T {

        if (context is BaseActivity) {
            //just be nice and handle it as DoubleDispatch call
            return componentProvider(context, clazz)
        }

        val sessionComponent = (context.applicationContext as DIComponentHolder<*>).component
        return sessionComponent.assertObjectCast(clazz)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Any.assertObjectCast(clazz: Class<T>): T {
        return this as? T
            ?: throw IllegalStateException("'${this.javaClass.name}' doesn't implement/extend '${clazz.name}'")
    }
}