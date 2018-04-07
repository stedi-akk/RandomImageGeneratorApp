package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator
import javax.inject.Inject

class ColoredNoiseParamsDialog : ButterKnifeDialogFragment(), ColoredNoiseParamsPresenter.UIImpl {

    private enum class MapedOrientation(val orientation: ColoredNoiseGenerator.Orientation, @StringRes val nameRes: Int) {
        VERTICAL(ColoredNoiseGenerator.Orientation.VERTICAL, R.string.vertical),
        HORIZONTAL(ColoredNoiseGenerator.Orientation.HORIZONTAL, R.string.horizontal),
        RANDOM(ColoredNoiseGenerator.Orientation.RANDOM, R.string.random);
    }

    private enum class MapedType(val type: ColoredNoiseGenerator.Type, @StringRes val nameRes: Int, val formatArg: String?) {
        TYPE_1(ColoredNoiseGenerator.Type.TYPE_1, R.string.type_s, "1"),
        TYPE_2(ColoredNoiseGenerator.Type.TYPE_2, R.string.type_s, "2"),
        TYPE_3(ColoredNoiseGenerator.Type.TYPE_3, R.string.type_s, "3"),
        TYPE_4(ColoredNoiseGenerator.Type.TYPE_4, R.string.type_s, "4"),
        TYPE_5(ColoredNoiseGenerator.Type.TYPE_5, R.string.type_s, "5"),
        TYPE_6(ColoredNoiseGenerator.Type.TYPE_6, R.string.type_s, "6"),
        RANDOM(ColoredNoiseGenerator.Type.RANDOM, R.string.random, null)
    }

    @Inject lateinit var presenter: ColoredNoiseParamsPresenter

    @BindView(R.id.colored_noise_params_dialog_sp_orientation) lateinit var spOrientation: Spinner
    @BindView(R.id.colored_noise_params_dialog_sp_type) lateinit var spType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GenerationStepsActivity).generationComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context as Context

        return AlertDialog.Builder(context).apply {
            setPositiveButton(R.string.ok) { _, _ ->
                presenter.setOrientation(MapedOrientation.values()[spOrientation.selectedItemPosition].orientation)
                presenter.setType(MapedType.values()[spType.selectedItemPosition].type)
            }
            setTitle(getString(R.string.s_parameters, getString(GeneratorType.COLORED_NOISE.nameRes)))
            setView(inflateAndBind(R.layout.colored_noise_params_dialog))
            spOrientation.adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, MapedOrientation.values().map { getString(it.nameRes) })
            spType.adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, MapedType.values().map { getString(it.nameRes, it.formatArg) })
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
    }
}