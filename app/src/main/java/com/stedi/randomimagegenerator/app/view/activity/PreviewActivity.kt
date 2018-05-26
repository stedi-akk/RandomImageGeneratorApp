package com.stedi.randomimagegenerator.app.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
    private lateinit var imageView: ImageView
    private lateinit var preset: Preset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PreviewActivityModel::class.java)
        viewModel.init(this)

        setContentView(R.layout.preview_activity)
        imageView = findViewById(R.id.preview_activity_image)
        imageView.setOnClickListener(this)

        viewModel.presenter.onAttach(this)
        preset = viewModel.presenter.getPreset()

        imageView.post {
            showNewPreviewImage()
        }
    }

    override fun onClick(v: View?) {
        showNewPreviewImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
    }

    override fun onImageSaved() {
        // TODO
    }

    override fun onImageFailedToSave() {
        // TODO
    }

    private fun showNewPreviewImage() {
        Picasso.get().load(RigRequestHandler.makePreviewUri(preset, imageView.width, imageView.width)).into(imageView)
    }
}