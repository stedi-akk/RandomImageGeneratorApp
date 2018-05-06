package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.other.nameRes
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import timber.log.Timber
import javax.inject.Inject

class SimpleIntegerParamsDialog : ButterKnifeDialogFragment(),
        TextWatcher,
        CompoundButton.OnCheckedChangeListener,
        SimpleIntegerParamsPresenter.UIImpl {

    @Inject lateinit var presenter: SimpleIntegerParamsPresenter

    @BindView(R.id.simple_integer_params_tv_input) lateinit var tvInput: TextView
    @BindView(R.id.simple_integer_params_dialog_et_value) lateinit var etValue: EditText
    @BindView(R.id.simple_integer_params_dialog_cb_random) lateinit var cbRandom: CheckBox

    private var isRandomValue: Boolean = false

    companion object {
        private const val KEY_TYPE = "KEY_TYPE"

        fun newInstance(type: GeneratorType): SimpleIntegerParamsDialog {
            if (arrayOf(GeneratorType.COLORED_CIRCLES, GeneratorType.COLORED_RECTANGLE, GeneratorType.COLORED_PIXELS).none { it == type }) {
                throw IllegalArgumentException(type.name + " is not supported")
            }

            return SimpleIntegerParamsDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_TYPE, type)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val type = arguments?.getSerializable(KEY_TYPE) as GeneratorType

        val builder = AlertDialog.Builder(context!!)
        builder.setPositiveButton(R.string.ok, null)
        builder.setTitle(getString(R.string.s_parameters, getString(type.nameRes())))
        builder.setView(inflateAndBind(R.layout.simple_integer_params_dialog))

        when (type) {
            GeneratorType.COLORED_CIRCLES -> tvInput.setText(R.string.circles_count)
            GeneratorType.COLORED_RECTANGLE -> tvInput.setText(R.string.rectangles_count)
            GeneratorType.COLORED_PIXELS -> tvInput.setText(R.string.pixel_multiplier)
            else -> throw IllegalArgumentException(type.name + " is not supported")
        }

        isRandomValue = presenter.isRandomValue()
        if (isRandomValue) {
            etValue.addTextChangedListener(this)
            cbRandom.setOnCheckedChangeListener(this)
        } else {
            cbRandom.visibility = View.GONE
        }

        val dialog = builder.create()
        dialog.setOnShowListener { dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { apply() } }

        if (savedInstanceState == null) {
            presenter.getValues()
        }

        return dialog
    }

    override fun afterTextChanged(s: Editable) {
        Timber.d("afterTextChanged")
        setRandomCheckSilently(false)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Timber.d("onChecked $isChecked")
        setValueTextSilently("")
    }

    override fun showRandomValue() {
        Timber.d("showRandomValue")
        setValueTextSilently("")
        setRandomCheckSilently(true)
    }

    override fun showValue(value: Int) {
        Timber.d("showValue $value")
        setValueTextSilently(value.toString())
        if (isRandomValue) {
            setRandomCheckSilently(false)
        }
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
        val success: Boolean

        if (isRandomValue && cbRandom.isChecked) {
            presenter.setRandomValue()
            success = true
        } else {
            val input = etValue.text.toString()

            if (input.isEmpty()) {
                showErrorIncorrectValue()
                return
            }

            try {
                success = presenter.setValue(Integer.parseInt(input))
            } catch (e: NumberFormatException) {
                Timber.e(e)
                showErrorIncorrectValue()
                return
            }
        }

        if (success) {
            dismiss()
        }
    }

    private fun setValueTextSilently(text: String) {
        if (isRandomValue) {
            etValue.removeTextChangedListener(this)
        }

        etValue.setText(text)
        etValue.setSelection(text.length)
        etValue.error = null

        if (isRandomValue) {
            etValue.addTextChangedListener(this)
        }
    }

    private fun setRandomCheckSilently(check: Boolean) {
        cbRandom.setOnCheckedChangeListener(null)
        cbRandom.isChecked = check
        cbRandom.setOnCheckedChangeListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}