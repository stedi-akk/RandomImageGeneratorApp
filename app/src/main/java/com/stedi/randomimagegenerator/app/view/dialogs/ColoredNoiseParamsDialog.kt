package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.other.nameRes
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator
import timber.log.Timber
import javax.inject.Inject

class ColoredNoiseParamsDialog : ButterKnifeDialogFragment(), ColoredNoiseParamsPresenter.UIImpl {

    @Inject lateinit var presenter: ColoredNoiseParamsPresenter

    @BindView(R.id.colored_noise_params_dialog_sp_orientation) lateinit var spOrientation: Spinner
    @BindView(R.id.colored_noise_params_dialog_sp_type) lateinit var spType: Spinner
    @BindView(R.id.colored_noise_params_dialog_et_value) lateinit var etValue: EditText

    private val TYPES_SORTED = arrayOf(
            ColoredNoiseGenerator.Type.TYPE_4,
            ColoredNoiseGenerator.Type.TYPE_5,
            ColoredNoiseGenerator.Type.TYPE_1,
            ColoredNoiseGenerator.Type.TYPE_3,
            ColoredNoiseGenerator.Type.TYPE_6,
            ColoredNoiseGenerator.Type.TYPE_2,
            ColoredNoiseGenerator.Type.RANDOM
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!).apply {
            setPositiveButton(R.string.ok, null)
            setTitle(getString(R.string.s_parameters, getString(GeneratorType.COLORED_NOISE.nameRes())))
            setView(inflateAndBind(R.layout.colored_noise_params_dialog))
            spOrientation.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, ColoredNoiseGenerator.Orientation.values().map { getString(it.nameRes()) })
            spType.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, TYPES_SORTED.map { getString(it.nameRes()) })
        }

        val dialog = builder.create()
        dialog.setOnShowListener { dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { apply() } }

        if (savedInstanceState == null) {
            presenter.getValues()
        }

        return dialog
    }

    override fun showOrientation(orientation: ColoredNoiseGenerator.Orientation) {
        spOrientation.setSelection(orientation.ordinal)
    }

    override fun showType(type: ColoredNoiseGenerator.Type) {
        spType.setSelection(TYPES_SORTED.indexOf(type))
    }

    override fun showValue(value: Int) {
        val text = value.toString()
        etValue.setText(text)
        etValue.setSelection(text.length)
        etValue.error = null
    }

    override fun showErrorIncorrectValue() {
        etValue.error = getString(R.string.value_bigger_zero)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        presenter.onDestroy()
    }

    private fun apply() {
        presenter.setOrientation(ColoredNoiseGenerator.Orientation.values()[spOrientation.selectedItemPosition])
        presenter.setType(TYPES_SORTED[spType.selectedItemPosition])

        val input = etValue.text.toString()

        if (input.isEmpty()) {
            showErrorIncorrectValue()
            return
        }

        try {
            if (presenter.setValue(Integer.parseInt(input))) {
                dismiss()
            }
        } catch (e: NumberFormatException) {
            Timber.e(e)
            showErrorIncorrectValue()
        }
    }

    override fun showRandomValue() {}
}