package com.stedi.randomimagegenerator.app.view.fragments

import android.Manifest
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.formatTime
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.other.showToast
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.EditPresetNameDialog
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment
import javax.inject.Inject

class ApplyGenerationFragment : StepFragment(), ApplyGenerationPresenter.UIImpl {
    private val REQUEST_CODE_WRITE_EXTERNAL = 22
    private val KEY_APPLY_GENERATION_PRESENTER_STATE = "KEY_APPLY_GENERATION_PRESENTER_STATE"

    @Inject lateinit var presenter: ApplyGenerationPresenter
    @Inject lateinit var bus: CachedBus
    @Inject lateinit var logger: Logger

    @BindView(R.id.apply_generation_fragment_tv) lateinit var tvOut: TextView
    @BindView(R.id.apply_generation_fragment_btn_save) lateinit var btnSave: Button

    private var startGenerationPreset: Preset? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GenerationStepsActivity).generationComponent.inject(this)
        presenter.onAttach(this)
        savedInstanceState?.apply {
            getSerializable(KEY_APPLY_GENERATION_PRESENTER_STATE)?.apply {
                presenter.onRestore(this)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.apply_generation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshFromPreset()
    }

    override fun onStart() {
        super.onStart()
        bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
    }

    override fun onSelected() {
        refreshFromPreset()
    }

    @OnClick(R.id.apply_generation_fragment_btn_save)
    fun onSaveClick(v: View) {
        val preset = presenter.getPreset()
        EditPresetNameDialog.newInstance(if (preset.id == 0) "" else preset.name).show(fragmentManager!!)
    }

    @OnClick(R.id.apply_generation_fragment_btn_generate)
    fun onGenerateClick(v: View) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            presenter.startGeneration()
        } else {
            startGenerationPreset = presenter.getPreset()
        }
    }

    override fun onPresetSaved() {
        activity?.finish()
    }

    override fun failedToSavePreset() {
        activity?.showToast(R.string.failed_save_preset, Toast.LENGTH_LONG)
    }

    override fun showGenerationDialog(preset: Preset) {
        GenerationDialog.newInstance(preset).show(fragmentManager!!)
    }

    @Subscribe
    fun onEditedPresetName(onEdited: EditPresetNameDialog.OnEdited) {
        presenter.savePreset(onEdited.name)
    }

    @Subscribe
    fun onPermissionEvent(event: BaseActivity.PermissionEvent) {
        if (event.requestCode == REQUEST_CODE_WRITE_EXTERNAL) {
            startGenerationPreset?.apply {
                if (event.isGranted) {
                    presenter.startGeneration()
                }
                startGenerationPreset = null
            }
        }
    }

    @Subscribe
    fun onGenerationDialogOnDismissed(event: GenerationDialog.OnDismissed) {
        activity?.finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_APPLY_GENERATION_PRESENTER_STATE, presenter.onRetain())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    private fun refreshFromPreset() {
        if (view != null) {
            tvOut.text = getSummaryFromPreset(presenter.getPreset())
            btnSave.setText(if (presenter.isPresetNew() || presenter.isPresetChanged()) R.string.unsaved_save else R.string.save)
        }
    }

    private fun getSummaryFromPreset(preset: Preset): String {
        return StringBuilder().apply {
            append(getString(R.string.name_s, preset.name))
            append("\n\n")
            if (preset.timestamp != 0L) {
                append(getString(R.string.created_s, formatTime(preset.timestamp)))
                append("\n\n")
            }

            val generatorParams = preset.getGeneratorParams()
            if (generatorParams is EffectGeneratorParams) {
                append(getString(R.string.generator_type_s, getString(generatorParams.target.getType().nameRes)))
                append("\n\n")
                append(getString(R.string.effect_type_s, getString(generatorParams.getType().nameRes)))
                append("\n\n")
            } else {
                append(getString(R.string.generator_type_s, getString(generatorParams.getType().nameRes)))
                append("\n\n")
            }

            var showCount = true
            if (appendRangeSize(this, R.string.width_s, preset.getWidthRange())) {
                showCount = false
            } else {
                append(getString(R.string.width_s, preset.getWidth().toString()))
            }
            append("\n\n")
            if (appendRangeSize(this, R.string.height_s, preset.getHeightRange())) {
                showCount = false
            } else {
                append(getString(R.string.height_s, preset.getHeight().toString()))
            }
            append("\n\n")
            if (showCount) {
                append(getString(R.string.count_s, preset.getCount().toString()))
                append("\n\n")
            }

            append(getString(R.string.quality_s_percent, preset.getQuality().format.name, preset.getQuality().qualityValue.toString()))
            append("\n\n")
            append(getString(R.string.folder_s, preset.pathToSave))
        }.toString()
    }

    private fun appendRangeSize(sb: StringBuilder, @StringRes res: Int, size: IntArray?): Boolean {
        if (size == null) {
            return false
        }
        val from = size[0].toString()
        val to = size[1].toString()
        val step = size[2].toString()
        sb.append(getString(res, getString(R.string.from_s_to_s_step_s, from, to, step)))
        return true
    }
}