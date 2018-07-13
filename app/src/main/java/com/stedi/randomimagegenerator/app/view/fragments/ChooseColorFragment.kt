package com.stedi.randomimagegenerator.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment

class ChooseColorFragment : GenerationFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_color_fragment, container, false)
    }
}