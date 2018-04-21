package com.stedi.randomimagegenerator.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment
import timber.log.Timber
import javax.inject.Inject

class ChooseEffectFragmentModel : BaseViewModel<ChooseEffectFragment>() {
    @Inject lateinit var presenter: ChooseEffectPresenter

    override fun onCreate(view: ChooseEffectFragment) {
        Timber.d("ChooseEffectFragmentModel onCreate")
        view.generationComponent.inject(this)
    }
}

class ChooseEffectFragment : GenerationFragment(), GeneratorTypeAdapter.ClickListener, ChooseEffectPresenter.UIImpl {

    private lateinit var viewModel: ChooseEffectFragmentModel

    @BindView(R.id.choose_effect_fragment_recycler_view) lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChooseEffectFragmentModel::class.java)
        viewModel.init(this)

        viewModel.presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_effect_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.generator_type_adapter_grid_items))
        recyclerView.setHasFixedSize(true)
        viewModel.presenter.getEffectTypes()
    }

    override fun onSelected() {
        if (view != null) {
            viewModel.presenter.getEffectTypes()
        }
    }

    override fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType?, targetType: GeneratorType) {
        recyclerView.adapter = GeneratorTypeAdapter(activity!!, types, selectedType, targetType, this, true)
    }

    override fun onSelected(type: GeneratorType) {
        Timber.d("onSelected: ${type.name}")
        viewModel.presenter.chooseEffectType(type)
    }

    override fun onDeselected() {
        Timber.d("onDeselected")
        viewModel.presenter.chooseEffectType(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
    }

    override fun onEditSelected() {}
}