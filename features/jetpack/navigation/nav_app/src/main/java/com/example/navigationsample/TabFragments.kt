package com.example.navigationsample

import android.os.Bundle
import android.view.View
import com.example.common.BaseFragment

class TabFragmentLeftStep1 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Next"
        button2.text = "TabFragmentRightStep1"
    }

    override fun onButton1Click(v: View) {
        TabFragmentLeftStep1Directions
            .actionTabFragmentLeftStep1ToTabFragmentLeftStep2()
            .navigate()
    }

    override fun onButton2Click(v: View) {
        TabFragmentLeftStep1Directions
            .actionTabFragmentLeftStep1ToTabFragmentRightStep1()
            .navigate()
    }
}

class TabFragmentLeftStep2 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Next"
        button2.isEnabled = false
    }

    override fun onButton1Click(v: View) {
        TabFragmentLeftStep2Directions
            .actionTabFragmentLeftStep2ToTabFragmentRightStep2()
            .navigate()
    }

    override fun onButton2Click(v: View) {
        TabFragmentLeftStep1Directions
            .actionTabFragmentLeftStep1ToTabFragmentRightStep1()
            .navigate()
    }
}

class TabFragmentRightStep1 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Next"
        button2.isEnabled = false
    }
}

class TabFragmentRightStep2 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}