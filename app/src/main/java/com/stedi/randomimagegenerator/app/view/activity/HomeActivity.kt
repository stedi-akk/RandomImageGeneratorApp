package com.stedi.randomimagegenerator.app.view.activity

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
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
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.other.showToast
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.components.ListSpaceDecoration
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog
import javax.inject.Inject

class HomeActivityModel : BaseViewModel<HomeActivity>() {
    @Inject lateinit var presenter: HomePresenter
    @Inject lateinit var bus: LockedBus

    override fun onCreate(view: HomeActivity) {
        view.activityComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class HomeActivity : BaseActivity(), HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    private val KEY_PRESENTER_STATE = "KEY_PRESENTER_STATE"
    private val KEY_PERMISSION_PRESET = "KEY_PERMISSION_PRESET"

    private val REQUEST_CODE_WRITE_EXTERNAL = 123
    private val REQUEST_CONFIRM_DELETE = 321
    private val REQUEST_CONFIRM_GENERATE = 231

    private lateinit var viewModel: HomeActivityModel

    @BindView(R.id.home_activity_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.home_activity_empty_view) lateinit var emptyView: View
    @BindView(R.id.home_activity_fab) lateinit var fab: FloatingActionButton

    private lateinit var adapter: PresetsAdapter

    private var permissionPreset: Preset? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeActivityModel::class.java)
        val restorePresenter = !viewModel.isInitialized && savedInstanceState != null
        viewModel.init(this)

        setContentView(R.layout.home_activity)
        ButterKnife.bind(this)

        savedInstanceState?.apply {
            if (restorePresenter) {
                viewModel.presenter.onRestore(getSerializable(KEY_PRESENTER_STATE))
            }
            permissionPreset = getParcelable(KEY_PERMISSION_PRESET)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(ListSpaceDecoration(dim2px(R.dimen.common_v_spacing), dim2px(R.dimen.common_lr_spacing)))
        adapter = PresetsAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(recyclerScrollListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.presenter.onAttach(this)
        viewModel.bus.register(this)
        viewModel.presenter.fetchPresets()
    }

    override fun onStop() {
        super.onStop()
        viewModel.bus.unregister(this)
        viewModel.presenter.onDetach()
    }

    @OnClick(R.id.home_activity_fab)
    fun onFabClick(v: View) {
        viewModel.presenter.newPreset()
    }

    override fun onCardClick(preset: Preset) {
        viewModel.presenter.editPreset(preset)
    }

    override fun onDeleteClick(preset: Preset) {
        viewModel.presenter.deletePreset(preset)
    }

    override fun onGenerateClick(preset: Preset) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            viewModel.presenter.startGeneration(preset)
        } else {
            permissionPreset = preset
        }
    }

    override fun onSaveClick(preset: Preset) {
        viewModel.presenter.editPreset(preset)
    }

    override fun onPresetsFetched(pendingPreset: Preset?, presets: List<Preset>) {
        adapter.set(presets, pendingPreset)
        refreshEmptyView()
    }

    override fun onFailedToFetchPresets() {
        showToast(R.string.failed_fetch_presets)
        refreshEmptyView()
    }

    override fun showConfirmDeletePreset(preset: Preset) {
        ConfirmDialog.newInstance(REQUEST_CONFIRM_DELETE,
                getString(R.string.confirm_action), getString(R.string.are_you_sure_delete_s_preset, preset.name))
                .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
    }

    override fun onPresetDeleted(preset: Preset) {
        adapter.remove(preset)
        refreshEmptyView()
    }

    override fun onFailedToDeletePreset() {
        showToast(R.string.failed_delete_preset)
    }

    override fun showConfirmGeneratePreset(preset: Preset) {
        ConfirmDialog.newInstance(REQUEST_CONFIRM_GENERATE,
                getString(R.string.confirm_action), getString(R.string.are_you_sure_generate_preset_s, preset.name))
                .show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
    }

    override fun showGenerationDialog(preset: Preset) {
        GenerationDialog.newInstance(preset).show(supportFragmentManager)
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
            permissionPreset?.apply {
                permissionPreset = null
                if (event.isGranted) {
                    viewModel.presenter.startGeneration(this)
                }
            }
        }
    }

    @Subscribe
    fun onConfirmDialogCallback(callback: ConfirmDialog.Callback) {
        if (callback.requestCode == REQUEST_CONFIRM_DELETE) {
            viewModel.presenter.confirmDeletePreset(callback.confirm)
        } else if (callback.requestCode == REQUEST_CONFIRM_GENERATE) {
            viewModel.presenter.confirmStartGeneration(callback.confirm)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_PRESENTER_STATE, viewModel.presenter.onRetain())
        outState.putParcelable(KEY_PERMISSION_PRESET, permissionPreset)
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