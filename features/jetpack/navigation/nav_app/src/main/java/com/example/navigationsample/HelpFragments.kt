package com.example.navigationsample

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.common.BaseFragment

class HelpFragmentStep1 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Go back"
    }

    override fun onButton1Click(v: View) {
        super.onButton1Click(v)
        findNavController().navigateUp()
    }

    override fun onButton2Click(v: View) {
        super.onButton2Click(v)
        HelpFragmentStep1Directions.actionHelpFragmentStep1ToHelpFragmentStep2().navigate()
    }
}

class HelpFragmentStep2 : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Finish"
        button2.isEnabled = false
    }

    override fun onButton1Click(v: View) {
        super.onButton1Click(v)
        if (requireActivity() is NavigationTabActivity) {
            toast("We are in TabActivity")
        } else {
            //we can't really make this in tab activity
            parentFragment?.findNavController()
                ?.navigate(HelpFragmentStep2Directions.actionHelpFragmentStep1Finish())
        }
    }
}