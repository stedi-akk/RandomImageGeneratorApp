package com.stedi.randomimagegenerator.app.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
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
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment
import javax.inject.Inject

class ChooseSaveOptionsFragment : StepFragment(),
        RadioGroup.OnCheckedChangeListener,
        NumberPicker.OnValueChangeListener,
        ChooseSaveOptionsPresenter.UIImpl {

    private val KEY_QUALITY_FORMAT = "KEY_QUALITY_FORMAT"
    private val KEY_QUALITY_VALUE = "KEY_QUALITY_VALUE"

    @Inject lateinit var presenter: ChooseSaveOptionsPresenter
    @Inject lateinit var logger: Logger

    @BindView(R.id.choose_save_options_fragment_rg_format) lateinit var rgFormat: RadioGroup
    @BindView(R.id.choose_save_options_fragment_value_picker) lateinit var npQuality: NumberPicker

    private var selectedFormat: Bitmap.CompressFormat? = null
    private var selectedValue = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GenerationStepsActivity).generationComponent.inject(this)
        presenter.onAttach(this)
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
        setDividerColor(npQuality, resources.getColor(R.color.colorAccent))
        rgFormat.setOnCheckedChangeListener(this)
        if (savedInstanceState == null) {
            presenter.getData()
        } else {
            selectedFormat = savedInstanceState.getSerializable(KEY_QUALITY_FORMAT) as Bitmap.CompressFormat?
            selectedValue = savedInstanceState.getInt(KEY_QUALITY_VALUE)
            refresh()
        }
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

    override fun onSelected() {
        if (view == null) {
            return
        }
        refresh()
    }

    private fun refresh() {
        val selectedFormat = selectedFormat
        if (selectedFormat == null || selectedValue == -1) {
            return
        }
        rgFormat.setOnCheckedChangeListener(null)
        rgFormat.clearCheck()
        rgFormat.check(selectedFormat.ordinal)
        rgFormat.setOnCheckedChangeListener(this)
        npQuality.setOnValueChangedListener(null)
        npQuality.value = selectedValue
        npQuality.setOnValueChangedListener(this)
    }

    override fun showQualityFormat(format: Bitmap.CompressFormat) {
        selectedFormat = format
    }

    override fun showQualityValue(value: Int) {
        selectedValue = value
    }

    override fun onCheckedChanged(group: RadioGroup, @IdRes checkedId: Int) {
        val format = Bitmap.CompressFormat.values()[checkedId]
        selectedFormat = format
        presenter.setQualityFormat(format)
    }

    override fun onValueChange(picker: NumberPicker, oldVal: Int, newVal: Int) {
        selectedValue = newVal
        presenter.setQualityValue(selectedValue)
    }

    override fun onIncorrectQualityValue() {
        logger.log(this, "onIncorrectQualityValue")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_QUALITY_FORMAT, selectedFormat)
        outState.putInt(KEY_QUALITY_VALUE, selectedValue)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    // https://stackoverflow.com/questions/24233556/changing-numberpicker-divider-color
    private fun setDividerColor(picker: NumberPicker, color: Int) {
        for (pf in NumberPicker::class.java.declaredFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    pf.set(picker, ColorDrawable(color))
                } catch (e: Exception) {
                    // ignore
                }
                break
            }
        }
    }
}