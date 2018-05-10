package com.stedi.randomimagegenerator.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.widget.AppCompatRadioButton
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioGroup
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.dp2px
import com.stedi.randomimagegenerator.app.other.setDividerColor
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment
import timber.log.Timber
import javax.inject.Inject

class ChooseSaveOptionsFragmentModel : BaseViewModel<ChooseSaveOptionsFragment>() {
    @Inject lateinit var presenter: ChooseSaveOptionsPresenter

    override fun onCreate(view: ChooseSaveOptionsFragment) {
        view.generationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class ChooseSaveOptionsFragment : GenerationFragment(),
        RadioGroup.OnCheckedChangeListener,
        NumberPicker.OnValueChangeListener,
        ChooseSaveOptionsPresenter.UIImpl {

    private lateinit var viewModel: ChooseSaveOptionsFragmentModel

    @BindView(R.id.choose_save_options_fragment_rg_format) lateinit var rgFormat: RadioGroup
    @BindView(R.id.choose_save_options_fragment_value_picker) lateinit var npQuality: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChooseSaveOptionsFragmentModel::class.java)
        viewModel.init(this)

        viewModel.presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_save_options_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFormatButtons(context!!)

        npQuality.minValue = 0
        npQuality.maxValue = 100
        npQuality.setOnValueChangedListener(this)
        rgFormat.setOnCheckedChangeListener(this)

        viewModel.presenter.getValues()
    }

    private fun addFormatButtons(context: Context) {
        val height = context.dp2px(48f).toInt()
        for (format in Bitmap.CompressFormat.values()) {
            val rb = AppCompatRadioButton(context)
            rb.id = format.ordinal
            rb.text = format.name
            rb.minHeight = height
            rb.gravity = Gravity.CENTER
            rgFormat.addView(rb)
        }
    }

    override fun showQualityFormat(format: Bitmap.CompressFormat) {
        rgFormat.setOnCheckedChangeListener(null)
        rgFormat.clearCheck()
        rgFormat.check(format.ordinal)
        rgFormat.jumpDrawablesToCurrentState()
        rgFormat.setOnCheckedChangeListener(this)
        updateQualityValuePicker(format)
    }

    override fun showQualityValue(value: Int) {
        npQuality.setOnValueChangedListener(null)
        npQuality.value = value
        npQuality.setOnValueChangedListener(this)
    }

    override fun onCheckedChanged(group: RadioGroup, @IdRes checkedId: Int) {
        val format = Bitmap.CompressFormat.values()[checkedId]
        viewModel.presenter.setQualityFormat(format)
        updateQualityValuePicker(format)
    }

    override fun onValueChange(picker: NumberPicker, oldVal: Int, newVal: Int) {
        viewModel.presenter.setQualityValue(newVal)
    }

    override fun onIncorrectQualityValue() {
        Timber.d("onIncorrectQualityValue")
        npQuality.value = 100
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
    }

    private fun updateQualityValuePicker(format: Bitmap.CompressFormat) {
        if (format == Bitmap.CompressFormat.PNG) {
            npQuality.setDividerColor(resources.getColor(R.color.gray_medium))
            npQuality.isEnabled = false
        } else {
            npQuality.setDividerColor(resources.getColor(R.color.colorAccent))
            npQuality.isEnabled = true
        }
    }
}