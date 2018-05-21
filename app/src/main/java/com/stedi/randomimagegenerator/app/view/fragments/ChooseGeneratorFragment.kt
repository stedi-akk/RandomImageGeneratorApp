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
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.dialogs.ColoredNoiseParamsDialog
import com.stedi.randomimagegenerator.app.view.dialogs.SimpleIntegerParamsDialog
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment
import javax.inject.Inject

class ChooseGeneratorFragmentModel : BaseViewModel<ChooseGeneratorFragment>() {
    @Inject lateinit var presenter: ChooseGeneratorPresenter

    override fun onCreate(view: ChooseGeneratorFragment) {
        view.generationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class ChooseGeneratorFragment : GenerationFragment(), ChooseGeneratorPresenter.UIImpl, GeneratorTypeAdapter.ClickListener {

    private lateinit var viewModel: ChooseGeneratorFragmentModel

    @BindView(R.id.choose_generator_fragment_recycler_view) lateinit var recyclerView: RecyclerView

    private lateinit var adapter: GeneratorTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChooseGeneratorFragmentModel::class.java)
        viewModel.init(this)

        viewModel.presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_generator_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.generator_type_adapter_grid_items))
        recyclerView.setHasFixedSize(true)
        adapter = GeneratorTypeAdapter(activity!!, this, false)
        recyclerView.adapter = adapter
        viewModel.presenter.getGeneratorTypes()
    }

    override fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType) {
        adapter.set(types, selectedType, null)
    }

    override fun onSelected(type: GeneratorType) {
        viewModel.presenter.chooseGeneratorType(type)
    }

    override fun onEditSelected() {
        viewModel.presenter.editChoseGeneratorParams()
    }

    override fun showEditGeneratorParams(type: GeneratorType) {
        val fragmentManager = fragmentManager ?: return
        when (type) {
            in SimpleIntegerParamsDialog.SUPPORTED_TYPES -> {
                SimpleIntegerParamsDialog.newInstance(type).show(fragmentManager)
            }
            GeneratorType.COLORED_NOISE -> {
                ColoredNoiseParamsDialog().show(fragmentManager)
            }
            else -> throw IllegalStateException("incorrect behavior")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.presenter.onDetach()
    }

    override fun onDeselected() {}
}