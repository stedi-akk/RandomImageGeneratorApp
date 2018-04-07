package com.stedi.randomimagegenerator.app.view.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader
import com.stedi.randomimagegenerator.app.view.dialogs.ColoredNoiseParamsDialog
import com.stedi.randomimagegenerator.app.view.dialogs.SimpleIntegerParamsDialog
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment
import javax.inject.Inject

class ChooseGeneratorFragment : StepFragment(), ChooseGeneratorPresenter.UIImpl, GeneratorTypeAdapter.ClickListener {

    @Inject lateinit var presenter: ChooseGeneratorPresenter
    @Inject lateinit var logger: Logger
    @Inject lateinit var adapterImageLoader: GeneratorTypeImageLoader

    @BindView(R.id.choose_generator_fragment_recycler_view) lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GenerationStepsActivity).generationComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_generator_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.generator_type_adapter_grid_items))
        recyclerView.setHasFixedSize(true)
        presenter.getGeneratorTypes()
    }

    override fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType) {
        recyclerView.adapter = GeneratorTypeAdapter(adapterImageLoader, types, selectedType, null, this, false)
    }

    override fun onSelected(type: GeneratorType) {
        presenter.chooseGeneratorType(type)
    }

    override fun onEditSelected() {
        presenter.editChoseGeneratorParams()
    }

    override fun showEditGeneratorParams(type: GeneratorType) {
        when (type) {
            GeneratorType.COLORED_PIXELS, GeneratorType.COLORED_CIRCLES, GeneratorType.COLORED_RECTANGLE -> SimpleIntegerParamsDialog.newInstance(type).show(fragmentManager!!)
            GeneratorType.COLORED_NOISE -> ColoredNoiseParamsDialog().show(fragmentManager!!)
            else -> throw IllegalStateException("incorrect behavior")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onDeselected() {}
}