package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.other.nameRes
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator
import javax.inject.Inject

class ColoredNoiseParamsDialog : ButterKnifeDialogFragment(), ColoredNoiseParamsPresenter.UIImpl {

    @Inject lateinit var presenter: ColoredNoiseParamsPresenter

    @BindView(R.id.colored_noise_params_dialog_sp_orientation) lateinit var spOrientation: Spinner
    @BindView(R.id.colored_noise_params_dialog_sp_type) lateinit var spType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!).apply {
            setPositiveButton(R.string.ok) { _, _ ->
                presenter.setOrientation(ColoredNoiseGenerator.Orientation.values()[spOrientation.selectedItemPosition])
                presenter.setType(ColoredNoiseGenerator.Type.values()[spType.selectedItemPosition])
            }
            setTitle(getString(R.string.s_parameters, getString(GeneratorType.COLORED_NOISE.nameRes())))
            setView(inflateAndBind(R.layout.colored_noise_params_dialog))
            spOrientation.adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ColoredNoiseGenerator.Orientation.values().map { getString(it.nameRes()) })
            spType.adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ColoredNoiseGenerator.Type.values().map { getString(it.nameRes()) })
            if (savedInstanceState == null) {
                presenter.getValues()
            }
        }.create()
    }

    override fun showOrientation(orientation: ColoredNoiseGenerator.Orientation) {
        spOrientation.setSelection(orientation.ordinal)
    }

    override fun showType(type: ColoredNoiseGenerator.Type) {
        spType.setSelection(type.ordinal)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        presenter.onDestroy()
    }
}