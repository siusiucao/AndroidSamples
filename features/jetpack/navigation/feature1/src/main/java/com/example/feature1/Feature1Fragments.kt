package com.example.feature1

import android.os.Bundle
import android.view.View
import com.example.common.BaseFragment

/*
adb shell am start -W -a android.intent.action.VIEW -d "https://navsample/feature1/22" com.example.navigationsample
adb shell am start -W -a android.intent.action.VIEW -d "https://navsample/feature1/12/finish?button1=xyz" com.example.navigationsample
 */
class Feature1FragmentStep1 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Next step"
        button2.text = "Navigate to Feature2"

        arguments?.let {
            args.text = "ID:${it.getString("id")}"
        }
    }

    override fun onButton1Click(v: View) {
        Feature1FragmentStep1Directions
            .actionF1FragmentStep1ToF1FragmentStep2()
            .navigate()
    }

    override fun onButton2Click(v: View) {
        Feature1FragmentStep1Directions
            .actionF1FragmentStep2ToF2Journey()
            .navigate()
    }
}

class Feature1FragmentStep2 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Close journey"
        button2.text = "Navigate to Feature2"

        arguments?.let {
            args.text = "ID:${it.getString("id")}\nButton1:${it.getString("button1")}"
        }
    }

    override fun onButton1Click(v: View) {
        Feature1FragmentStep2Directions
            .actionF1FragmentStep2LeaveJourney()
            .navigate()
    }

    override fun onButton2Click(v: View) {
        Feature1FragmentStep2Directions
            .actionF1FragmentStep2ToF2Journey()
            .navigate()
    }
}