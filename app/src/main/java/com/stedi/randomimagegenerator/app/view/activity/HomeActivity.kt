package com.stedi.randomimagegenerator.app.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.RootSavePath
import com.stedi.randomimagegenerator.app.di.modules.HomeModule
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.other.showToast
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader
import com.stedi.randomimagegenerator.app.view.components.ListSpaceDecoration
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog
import java.io.File
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    private val KEY_HOME_PRESENTER_STATE = "KEY_HOME_PRESENTER_STATE"
    private val REQUEST_CODE_WRITE_EXTERNAL = 123
    private val REQUEST_CONFIRM_DELETE = 321
    private val REQUEST_CONFIRM_GENERATE = 231

    @Inject lateinit var presenter: HomePresenter
    @Inject lateinit var logger: Logger
    @Inject lateinit var adapterImageLoader: GeneratorTypeImageLoader
    @Inject @field:RootSavePath lateinit var rootSavePath: String

    @BindView(R.id.home_activity_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.home_activity_empty_view) lateinit var emptyView: View
    @BindView(R.id.home_activity_fab) lateinit var fab: FloatingActionButton

    private lateinit var adapter: PresetsAdapter

    private var startGenerationPreset: Preset? = null
    private var confirmPresetName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.plus(HomeModule()).inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_activity)
        ButterKnife.bind(this)

        savedInstanceState?.apply {
            savedInstanceState.getSerializable(KEY_HOME_PRESENTER_STATE)?.apply {
                presenter.onRestore(this)
            }
        }

        fab.hide(fabShowHideListener)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(ListSpaceDecoration(dim2px(R.dimen.common_v_spacing), dim2px(R.dimen.common_lr_spacing)))
        adapter = PresetsAdapter(adapterImageLoader, rootSavePath, this)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(recyclerScrollListener)
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        bus.register(this)
        presenter.fetchPresets()
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
        presenter.onDetach()
    }

    override fun onPresetsFetched(pendingPreset: Preset?, presets: List<Preset>) {
        fab.show(fabShowHideListener)
        adapter.set(presets, pendingPreset)
        refreshEmptyView()
    }

    override fun onFailedToFetchPresets() {
        fab.show(fabShowHideListener)
        showToast(R.string.failed_fetch_presets)
        refreshEmptyView()
    }

    @OnClick(R.id.home_activity_fab)
    fun onFabClick(v: View) {
        GenerationStepsActivity.start(this, true)
    }

    override fun onCardClick(preset: Preset) {
        presenter.editPreset(preset)
    }

    override fun showEditPreset() {
        GenerationStepsActivity.start(this, false)
    }

    override fun onSaveClick(preset: Preset) {
        presenter.editPreset(preset)
    }

    override fun onDeleteClick(preset: Preset) {
        confirmPresetName = preset.name
        presenter.deletePreset(preset)
    }

    override fun onPresetDeleted(preset: Preset) {
        logger.log(this, "onPresetDeleted $preset")
        adapter.remove(preset)
        refreshEmptyView()
    }

    override fun onFailedToDeletePreset() {
        showToast(R.string.failed_delete_preset)
    }

    override fun onGenerateClick(preset: Preset) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            confirmPresetName = preset.name
            presenter.startGeneration(preset)
        } else {
            startGenerationPreset = preset
        }
    }

    override fun showConfirmLastAction(confirm: HomePresenter.Confirm) {
        if (confirm === HomePresenter.Confirm.DELETE_PRESET) {
            ConfirmDialog.newInstance(REQUEST_CONFIRM_DELETE,
                    getString(R.string.confirm_action), getString(R.string.are_you_sure_delete_s_preset, confirmPresetName))
                    .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
        } else if (confirm === HomePresenter.Confirm.GENERATE_FROM_PRESET) {
            ConfirmDialog.newInstance(REQUEST_CONFIRM_GENERATE,
                    getString(R.string.confirm_action), getString(R.string.are_you_sure_generate_preset_s, confirmPresetName))
                    .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
        }
    }

    @SuppressLint("MissingPermission")
    @Subscribe
    fun onPermissionEvent(event: BaseActivity.PermissionEvent) {
        if (event.requestCode == REQUEST_CODE_WRITE_EXTERNAL) {
            startGenerationPreset?.apply {
                if (event.isGranted) {
                    confirmPresetName = name
                    presenter.startGeneration(this)
                }
                startGenerationPreset = null
            }
        }
    }

    @Subscribe
    fun onConfirmDialogCallback(callback: ConfirmDialog.Callback) {
        logger.log(this, "onConfirmDialogCallback " + callback.confirm)
        if (callback.requestCode == REQUEST_CONFIRM_DELETE || callback.requestCode == REQUEST_CONFIRM_GENERATE) {
            if (callback.confirm) {
                presenter.confirmLastAction()
            } else {
                presenter.cancelLastAction()
            }
        }
    }

    override fun onStartGeneration() {
        logger.log(this, "onStartGeneration")
        GenerationDialog.getInstance(supportFragmentManager).onStartGeneration()
    }

    override fun onGenerated(imageParams: ImageParams, imageFile: File) {
        logger.log(this, "onGenerated")
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)))
        GenerationDialog.getInstance(supportFragmentManager).onGenerated(imageParams, imageFile)
    }

    override fun onGenerationUnknownError() {
        logger.log(this, "onGenerationUnknownError")
        GenerationDialog.getInstance(supportFragmentManager).onGenerationUnknownError()
    }

    override fun onFailedToGenerate(imageParams: ImageParams) {
        logger.log(this, "onFailedToGenerate")
        GenerationDialog.getInstance(supportFragmentManager).onFailedToGenerate(imageParams)
    }

    override fun onFinishGeneration() {
        logger.log(this, "onFinishGeneration")
        GenerationDialog.getInstance(supportFragmentManager).onFinishGeneration()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_HOME_PRESENTER_STATE, presenter.onRetain())
    }

    private fun refreshEmptyView() {
        emptyView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private val recyclerScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0) {
                fab.hide(fabShowHideListener)
            } else {
                fab.show(fabShowHideListener)
            }
        }
    }

    private val fabShowHideListener = object : FloatingActionButton.OnVisibilityChangedListener() {
        override fun onShown(fab: FloatingActionButton) {
            fab.visibility = View.VISIBLE
        }

        override fun onHidden(fab: FloatingActionButton) {
            fab.visibility = View.GONE
        }
    }
}