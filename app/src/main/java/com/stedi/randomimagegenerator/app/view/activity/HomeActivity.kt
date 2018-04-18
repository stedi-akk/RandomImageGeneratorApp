package com.stedi.randomimagegenerator.app.view.activity

import android.Manifest
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.modules.HomeModule
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.other.showToast
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter
import com.stedi.randomimagegenerator.app.view.components.ListSpaceDecoration
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    private val KEY_HOME_PRESENTER_STATE = "KEY_HOME_PRESENTER_STATE"
    private val KEY_CONFIRM_PRESET = "KEY_CONFIRM_PRESET"
    private val REQUEST_CODE_WRITE_EXTERNAL = 123
    private val REQUEST_CONFIRM_DELETE = 321
    private val REQUEST_CONFIRM_GENERATE = 231

    @Inject lateinit var presenter: HomePresenter
    @Inject lateinit var logger: Logger

    @BindView(R.id.home_activity_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.home_activity_empty_view) lateinit var emptyView: View
    @BindView(R.id.home_activity_fab) lateinit var fab: FloatingActionButton

    private lateinit var adapter: PresetsAdapter

    private var confirmPreset: Preset? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.plus(HomeModule()).inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_activity)
        ButterKnife.bind(this)

        savedInstanceState?.apply {
            savedInstanceState.getSerializable(KEY_HOME_PRESENTER_STATE)?.apply {
                presenter.onRestore(this)
            }
            confirmPreset = savedInstanceState.getParcelable(KEY_CONFIRM_PRESET)
        }

        fab.hide(fabShowHideListener)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(ListSpaceDecoration(dim2px(R.dimen.common_v_spacing), dim2px(R.dimen.common_lr_spacing)))
        adapter = PresetsAdapter(this)
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

    @OnClick(R.id.home_activity_fab)
    fun onFabClick(v: View) {
        presenter.newPreset()
    }

    override fun onCardClick(preset: Preset) {
        presenter.editPreset(preset)
    }

    override fun onDeleteClick(preset: Preset) {
        confirmPreset = preset
        presenter.deletePreset(preset)
    }

    override fun onGenerateClick(preset: Preset) {
        confirmPreset = preset
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            presenter.startGeneration(preset)
        }
    }

    override fun onSaveClick(preset: Preset) {
        presenter.editPreset(preset)
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

    override fun showConfirmDeletePreset() {
        val confirmPreset = confirmPreset ?: return
        ConfirmDialog.newInstance(REQUEST_CONFIRM_DELETE,
                getString(R.string.confirm_action), getString(R.string.are_you_sure_delete_s_preset, confirmPreset.name))
                .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
    }

    override fun onPresetDeleted(preset: Preset) {
        logger.log(this, "onPresetDeleted $preset")
        adapter.remove(preset)
        refreshEmptyView()
    }

    override fun onFailedToDeletePreset() {
        showToast(R.string.failed_delete_preset)
    }

    override fun showConfirmGeneratePreset() {
        val confirmPreset = confirmPreset ?: return
        ConfirmDialog.newInstance(REQUEST_CONFIRM_GENERATE,
                getString(R.string.confirm_action), getString(R.string.are_you_sure_generate_preset_s, confirmPreset.name))
                .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
    }

    override fun showGenerationDialog() {
        val confirmPreset = confirmPreset ?: return
        GenerationDialog.newInstance(confirmPreset).show(supportFragmentManager)
    }

    override fun showEditPreset() {
        GenerationStepsActivity.start(this, false)
    }

    override fun showCreatePreset() {
        GenerationStepsActivity.start(this, true)
    }

    @Subscribe
    fun onPermissionEvent(event: BaseActivity.PermissionEvent) {
        if (event.requestCode == REQUEST_CODE_WRITE_EXTERNAL) {
            confirmPreset?.apply {
                if (event.isGranted) {
                    presenter.startGeneration(this)
                }
            }
        }
    }

    @Subscribe
    fun onConfirmDialogCallback(callback: ConfirmDialog.Callback) {
        logger.log(this, "onConfirmDialogCallback " + callback.confirm)
        if (callback.requestCode == REQUEST_CONFIRM_DELETE) {
            presenter.confirmDeletePreset(callback.confirm)
        } else if (callback.requestCode == REQUEST_CONFIRM_GENERATE) {
            presenter.confirmStartGeneration(callback.confirm)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_HOME_PRESENTER_STATE, presenter.onRetain())
        outState.putParcelable(KEY_CONFIRM_PRESET, confirmPreset)
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