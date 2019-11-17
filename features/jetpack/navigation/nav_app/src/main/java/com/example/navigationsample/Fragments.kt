package com.example.navigationsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.common.BaseFragment
import com.example.common.appPrefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppStartFragment/*SplashScreen*/ : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val target = if (appPrefs.isLoggedIn) {
            AppStartFragmentDirections.actionFragmentMainToFragmentHome()
        } else {
            AppStartFragmentDirections.actionFragmentStartToNavLoginJourney()
        }
        target.navigate()
    }
}

class HomeFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container.addView(LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            addView(Button(requireContext()).apply {
                text = "Help"
                setOnClickListener {
                    HomeFragmentDirections
                        .actionFragmentHomeToHelp()
                        .navigate()
                }
            })
            addView(Button(requireContext()).apply {
                text = "TabActivity"
                setOnClickListener {
                    HomeFragmentDirections
                        .actionFragmentHomeToTabActivity()
                        .navigate()
                }
            })
        })
        super.onViewCreated(view, savedInstanceState)
        button1.text = "Feature1"
        button2.text = "Feature2"
    }

    override fun onButton1Click(v: View) {
        super.onButton1Click(v)
        navigate(R.id.nav_feature1_journey)
    }

    override fun onButton2Click(v: View) {
        super.onButton2Click(v)
        HomeFragmentDirections.actionFragmentHomeToNavFeature2Journey().navigate()
    }
}

class HelpBottomSheetFragmentFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nav_fragment_help, container, false)
    }
}

class HelpContainerFragmentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nav_fragment_help, container, false)
    }
}
