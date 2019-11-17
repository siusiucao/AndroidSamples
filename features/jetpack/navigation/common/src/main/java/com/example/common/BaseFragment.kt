@file:Suppress("MemberVisibilityCanBePrivate")

package com.example.common

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavDirections
import androidx.navigation.debugPrintBackStack
import androidx.navigation.fragment.findNavController

interface CheckingLoginStateActivity {
    fun requireLoggedIn(): Boolean
}

open class BaseFragment : Fragment(R.layout.common_fragment), LifecycleObserver {
    protected val container by lazyView<ViewGroup>(R.id.container)
    protected val textView by lazyView<TextView>(R.id.textView)
    protected val args by lazyView<TextView>(R.id.args)
    protected val button1 by lazyView<Button>(R.id.button1)
    protected val button2 by lazyView<Button>(R.id.button2)
    protected val backStack by lazyView<TextView>(R.id.back_stack)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.text = this::class.java.name
        button1.setOnClickListener(::onButton1Click)
        button2.setOnClickListener(::onButton2Click)
    }

    override fun onStart() {
        super.onStart()
        dispatchCheckLoggedIn()
    }

    protected open fun dispatchCheckLoggedIn(): Boolean {
        return (requireActivity() as? CheckingLoginStateActivity)
            ?.requireLoggedIn()
            ?: throw IllegalStateException("activity is not CheckingLoginStateActivity")
    }

    open fun onButton1Click(v: View) {}
    open fun onButton2Click(v: View) {}

    override fun onResume() {
        super.onResume()
        backStack.text = "Backstack:\n${findNavController().debugPrintBackStack()}"
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAnyLifecycleEvent(source: LifecycleOwner, ev: Lifecycle.Event) {
        Log.d("Lifecycle", "${this::class.java.simpleName}:${ev.name}")
        if (ev == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
        }
    }

    fun NavDirections.navigate() = findNavController().navigate(this)
    fun navigate(@IdRes destination: Int) = findNavController().navigate(destination)
    fun toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}