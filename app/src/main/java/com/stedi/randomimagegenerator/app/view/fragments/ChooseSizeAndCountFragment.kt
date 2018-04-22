package com.stedi.randomimagegenerator.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.showToast
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment
import com.stepstone.stepper.VerificationError
import timber.log.Timber
import javax.inject.Inject

class ChooseSizeAndCountFragmentModel : BaseViewModel<ChooseSizeAndCountFragment>() {
    @Inject lateinit var presenter: ChooseSizeAndCountPresenter

    override fun onCreate(view: ChooseSizeAndCountFragment) {
        view.generationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class ChooseSizeAndCountFragment : GenerationFragment(),
        TextWatcher,
        ChooseSizeAndCountPresenter.UIImpl {

    private lateinit var viewModel: ChooseSizeAndCountFragmentModel

    @BindView(R.id.choose_size_and_count_et_width) lateinit var etWidth: EditText
    @BindView(R.id.choose_size_and_count_et_height) lateinit var etHeight: EditText
    @BindView(R.id.choose_size_and_count_et_count) lateinit var etCount: EditText
    @BindView(R.id.choose_size_and_count_et_width_range_from) lateinit var etWidthRangeFrom: EditText
    @BindView(R.id.choose_size_and_count_et_width_range_to) lateinit var etWidthRangeTo: EditText
    @BindView(R.id.choose_size_and_count_et_width_range_step) lateinit var etWidthRangeStep: EditText
    @BindView(R.id.choose_size_and_count_et_height_range_from) lateinit var etHeightRangeFrom: EditText
    @BindView(R.id.choose_size_and_count_et_height_range_to) lateinit var etHeightRangeTo: EditText
    @BindView(R.id.choose_size_and_count_et_height_range_step) lateinit var etHeightRangeStep: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChooseSizeAndCountFragmentModel::class.java)
        viewModel.init(this)

        viewModel.presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_size_and_count_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etWidth.addTextChangedListener(this)
        etHeight.addTextChangedListener(this)
        etCount.addTextChangedListener(this)
        etWidthRangeFrom.addTextChangedListener(this)
        etWidthRangeTo.addTextChangedListener(this)
        etWidthRangeStep.addTextChangedListener(this)
        etHeightRangeFrom.addTextChangedListener(this)
        etHeightRangeTo.addTextChangedListener(this)
        etHeightRangeStep.addTextChangedListener(this)
        if (savedInstanceState == null) {
            viewModel.presenter.getValues()
        }
    }

    override fun onSelected() {
        if (view != null) {
            etWidth.setSelection(etWidth.text.length)
            etWidth.requestFocus()
        }
    }

    override fun afterTextChanged(s: Editable) {
        if (etWidth.hasFocus()) {
            Timber.d("afterTextChanged for etWidth")
            clearSilently(etWidthRangeFrom, etWidthRangeTo, etWidthRangeStep)
            if (!isHeightRangeInEdit()) {
                fillIfEmptySilently(etHeight, etCount)
                viewModel.presenter.setWidth(1)
                viewModel.presenter.setHeight(getValue(etHeight))
                viewModel.presenter.setCount(getValue(etCount))
            }
            if (isEmpty(etWidth) || getValue(etWidth) == 0) {
                viewModel.presenter.setWidth(1)
                etWidth.error = getString(R.string.value_bigger_zero)
                return
            }
            viewModel.presenter.setWidth(getValue(etWidth))
        } else if (etHeight.hasFocus()) {
            Timber.d("afterTextChanged for etHeight")
            clearSilently(etHeightRangeFrom, etHeightRangeTo, etHeightRangeStep)
            if (!isWidthRangeInEdit()) {
                fillIfEmptySilently(etWidth, etCount)
                viewModel.presenter.setWidth(getValue(etWidth))
                viewModel.presenter.setHeight(1)
                viewModel.presenter.setCount(getValue(etCount))
            }
            if (isEmpty(etHeight) || getValue(etHeight) == 0) {
                viewModel.presenter.setHeight(1)
                etHeight.error = getString(R.string.value_bigger_zero)
                return
            }
            viewModel.presenter.setHeight(getValue(etHeight))
        } else if (etCount.hasFocus()) {
            Timber.d("afterTextChanged for etCount")
            clearSilently(etWidthRangeFrom, etWidthRangeTo, etWidthRangeStep, etHeightRangeFrom, etHeightRangeTo, etHeightRangeStep)
            fillIfEmptySilently(etWidth, etHeight)
            viewModel.presenter.setWidth(getValue(etWidth))
            viewModel.presenter.setHeight(getValue(etHeight))
            if (isEmpty(etCount) || getValue(etCount) == 0) {
                etCount.error = getString(R.string.value_bigger_zero)
                return
            }
            viewModel.presenter.setCount(getValue(etCount))
        } else if (etWidthRangeFrom.hasFocus()) {
            Timber.d("afterTextChanged for etWidthRangeFrom")
            fillIfEmptySilently(etWidthRangeTo, etWidthRangeStep)
            afterWidthRangeTextChanged(etWidthRangeFrom)
        } else if (etWidthRangeTo.hasFocus()) {
            Timber.d("afterTextChanged for etWidthRangeTo")
            fillIfEmptySilently(etWidthRangeFrom, etWidthRangeStep)
            afterWidthRangeTextChanged(etWidthRangeTo)
        } else if (etWidthRangeStep.hasFocus()) {
            Timber.d("afterTextChanged for etWidthRangeStep")
            fillIfEmptySilently(etWidthRangeFrom, etWidthRangeTo)
            afterWidthRangeTextChanged(etWidthRangeStep)
        } else if (etHeightRangeFrom.hasFocus()) {
            Timber.d("afterTextChanged for etHeightRangeFrom")
            fillIfEmptySilently(etHeightRangeTo, etHeightRangeStep)
            afterHeightRangeTextChanged(etHeightRangeFrom)
        } else if (etHeightRangeTo.hasFocus()) {
            Timber.d("afterTextChanged for etHeightRangeTo")
            fillIfEmptySilently(etHeightRangeFrom, etHeightRangeStep)
            afterHeightRangeTextChanged(etHeightRangeTo)
        } else if (etHeightRangeStep.hasFocus()) {
            Timber.d("afterTextChanged for etHeightRangeStep")
            fillIfEmptySilently(etHeightRangeFrom, etHeightRangeTo)
            afterHeightRangeTextChanged(etHeightRangeStep)
        }
    }

    private fun afterWidthRangeTextChanged(etRangeType: EditText) {
        clearSilently(etWidth, etCount)
        if (isEmpty(etRangeType) || getValue(etRangeType) == 0) {
            etRangeType.error = getString(R.string.value_bigger_zero)
            return
        }
        viewModel.presenter.setWidthRange(getValue(etWidthRangeFrom), getValue(etWidthRangeTo), getValue(etWidthRangeStep))
    }

    private fun afterHeightRangeTextChanged(etRangeType: EditText) {
        clearSilently(etHeight, etCount)
        if (isEmpty(etRangeType) || getValue(etRangeType) == 0) {
            etRangeType.error = getString(R.string.value_bigger_zero)
            return
        }
        viewModel.presenter.setHeightRange(getValue(etHeightRangeFrom), getValue(etHeightRangeTo), getValue(etHeightRangeStep))
    }

    override fun showWidth(width: Int) {
        etWidth.setText(width.toString())
    }

    override fun showHeight(height: Int) {
        etHeight.setText(height.toString())
    }

    override fun showWidthRange(from: Int, to: Int, step: Int) {
        etWidthRangeFrom.setText(from.toString())
        etWidthRangeTo.setText(to.toString())
        etWidthRangeStep.setText(step.toString())
    }

    override fun showHeightRange(from: Int, to: Int, step: Int) {
        etHeightRangeFrom.setText(from.toString())
        etHeightRangeTo.setText(to.toString())
        etHeightRangeStep.setText(step.toString())
    }

    override fun showCount(count: Int) {
        etCount.setText(count.toString())
    }

    override fun onError(error: ChooseSizeAndCountPresenter.Error) {
        val errorString = getString(R.string.value_bigger_zero)
        when (error) {
            ChooseSizeAndCountPresenter.Error.INCORRECT_COUNT -> etCount.error = errorString
            ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH -> etWidth.error = errorString
            ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT -> etHeight.error = errorString
            ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH_RANGE -> {
                etWidthRangeFrom.error = errorString
                etWidthRangeTo.error = errorString
                etWidthRangeStep.error = errorString
            }
            ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT_RANGE -> {
                etHeightRangeFrom.error = errorString
                etHeightRangeTo.error = errorString
                etHeightRangeStep.error = errorString
            }
        }
    }

    override fun verifyStep(): VerificationError? {
        return if (hasAnyErrors()) VerificationError(getString(R.string.correct_errors)) else null
    }

    override fun onError(error: VerificationError) {
        context?.showToast(error.errorMessage, Toast.LENGTH_LONG)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
    }

    private fun isEmpty(et: EditText): Boolean {
        return et.text.toString().isEmpty()
    }

    private fun getValue(et: EditText): Int {
        try {
            return Integer.parseInt(et.text.toString())
        } catch (e: NumberFormatException) {
            Timber.e(e)
            return 0
        }
    }

    private fun fillIfEmptySilently(vararg editTexts: EditText) {
        for (et in editTexts) {
            val text = et.text.toString()
            if (!text.isEmpty() && getValue(et) > 0) {
                continue
            }
            et.removeTextChangedListener(this)
            et.setText("1")
            et.error = null
            et.addTextChangedListener(this)
        }
    }

    private fun clearSilently(vararg editTexts: EditText) {
        for (et in editTexts) {
            et.removeTextChangedListener(this)
            et.setText("")
            et.error = null
            et.addTextChangedListener(this)
        }
    }

    private fun isWidthRangeInEdit(): Boolean {
        return !etWidthRangeFrom.text.toString().isEmpty() ||
                !etWidthRangeTo.text.toString().isEmpty() ||
                !etWidthRangeStep.text.toString().isEmpty()
    }

    private fun isHeightRangeInEdit(): Boolean {
        return !etHeightRangeFrom.text.toString().isEmpty() ||
                !etHeightRangeTo.text.toString().isEmpty() ||
                !etHeightRangeStep.text.toString().isEmpty()
    }

    private fun hasAnyErrors(): Boolean {
        return etWidth.error != null || etHeight.error != null || etCount.error != null ||
                etWidthRangeFrom.error != null || etWidthRangeTo.error != null || etWidthRangeStep.error != null ||
                etHeightRangeFrom.error != null || etHeightRangeTo.error != null || etHeightRangeStep.error != null
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}