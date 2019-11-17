package com.example.feature2

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.common.BaseFragment

class Feature2FragmentStep1 : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Go back"
        button2.text = "Finish"
    }

    override fun onButton1Click(v: View) {
        findNavController().popBackStack()
    }

    override fun onButton2Click(v: View) {
        toast("Finished")
        Feature2FragmentStep1Directions.actionF2FragmentStep1LeaveJoruney().navigate()
    }
}

