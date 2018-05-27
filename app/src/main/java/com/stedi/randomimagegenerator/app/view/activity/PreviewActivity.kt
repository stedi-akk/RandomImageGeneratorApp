package com.stedi.randomimagegenerator.app.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.PreviewGenerationPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.components.RigRequestHandler
import javax.inject.Inject

class PreviewActivityModel : BaseViewModel<PreviewActivity>() {
    @Inject lateinit var presenter: PreviewGenerationPresenter

    override fun onCreate(view: PreviewActivity) {
        view.activityComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class PreviewActivity : BaseActivity(), View.OnClickListener, PreviewGenerationPresenter.UIImpl {

    private lateinit var viewModel: PreviewActivityModel

    @BindView(R.id.preview_activity_image) lateinit var imageView: ImageView
    @BindView(R.id.preview_activity_progress) lateinit var progressView: View

    private lateinit var preset: Preset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PreviewActivityModel::class.java)
        viewModel.init(this)

        setContentView(R.layout.preview_activity)
        ButterKnife.bind(this)

        imageView.setOnClickListener(this)
        showProgressBar(false)

        viewModel.presenter.onAttach(this)
        preset = viewModel.presenter.getPreset()

        imageView.post {
            generateNewPreviewImage()
        }
    }

    override fun onClick(v: View?) {
        generateNewPreviewImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
        Picasso.get().cancelRequest(imageView)
    }

    override fun onImageSaved() {
        // TODO
    }

    override fun onImageFailedToSave() {
        // TODO
    }

    private fun generateNewPreviewImage() {
        val imageSize = Math.min(imageView.width, imageView.height)
        showProgressBar(true)
        Picasso.get().load(RigRequestHandler.makePreviewUri(preset, imageSize, imageSize))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .noPlaceholder()
                .into(imageView, object : Callback.EmptyCallback() {
                    override fun onSuccess() {
                        showProgressBar(false)
                    }
                })
    }

    private fun showProgressBar(show: Boolean) {
        imageView.isClickable = !show
        imageView.isFocusable = !show
        progressView.visibility = if (show) View.VISIBLE else View.GONE
    }
}
