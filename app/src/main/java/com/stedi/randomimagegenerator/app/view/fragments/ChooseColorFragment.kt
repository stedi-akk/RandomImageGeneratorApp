package com.stedi.randomimagegenerator.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.view.components.HSVRangeBar
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment

class ChooseColorFragment : GenerationFragment() {

    @BindView(R.id.choose_color_fragment_range_bar) lateinit var rangeBar: HSVRangeBar
    @BindView(R.id.choose_color_fragment_cb_use_light) lateinit var cbUseLight: CheckBox
    @BindView(R.id.choose_color_fragment_cb_use_dark) lateinit var cbUseDark: CheckBox
    @BindView(R.id.choose_color_fragment_cb_grayscale) lateinit var cbGrayscale: CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_color_fragment, container, false)
    }
}