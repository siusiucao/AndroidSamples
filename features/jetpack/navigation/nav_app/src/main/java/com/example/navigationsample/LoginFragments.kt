package com.example.navigationsample

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.common.BaseFragment
import com.example.common.appPrefs

abstract class BaseLoginFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Previous Step"
        button2.text = "Next Step"
    }

    override fun onButton1Click(v: View) {
        findNavController().popBackStack()
    }

    override fun dispatchCheckLoggedIn(): Boolean {
        //we are not if we are in the logged in journey
        return false
    }
}

class LoginFragmentStep1 : BaseLoginFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.isEnabled = false
    }

    override fun onButton2Click(v: View) {
        LoginFragmentStep1Directions
            .actionFragmentLoginStep1ToFragmentLoginStep2()
            .navigate()
    }
}

class LoginFragmentStep2 : BaseLoginFragment() {
    override fun onButton2Click(v: View) {
        LoginFragmentStep2Directions
            .actionFragmentLoginStep2ToFragmentLoginStep3()
            .navigate()
    }
}

class LoginFragmentStep3 : BaseLoginFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button2.text = "Finish"
    }

    override fun onButton2Click(v: View) {
        appPrefs.isLoggedIn = true
        findNavController().currentDestination
        LoginFragmentStep3Directions
            .actionFragmentLoginStep3LeaveJourney()
            .navigate()
    }
}