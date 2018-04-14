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
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment
import javax.inject.Inject

class ChooseEffectFragment : StepFragment(), GeneratorTypeAdapter.ClickListener, ChooseEffectPresenter.UIImpl {

    @Inject lateinit var presenter: ChooseEffectPresenter
    @Inject lateinit var logger: Logger

    @BindView(R.id.choose_effect_fragment_recycler_view) lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GenerationStepsActivity).generationComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_effect_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.generator_type_adapter_grid_items))
        recyclerView.setHasFixedSize(true)
        presenter.getEffectTypes()
    }

    override fun onSelected() {
        if (view != null) {
            presenter.getEffectTypes()
        }
    }

    override fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType?, targetType: GeneratorType) {
        recyclerView.adapter = GeneratorTypeAdapter(types, selectedType, targetType, this, true)
    }

    override fun onSelected(type: GeneratorType) {
        logger.log(this, "onSelected: " + type.name)
        presenter.chooseEffectType(type)
    }

    override fun onDeselected() {
        logger.log(this, "onDeselected")
        presenter.chooseEffectType(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onEditSelected() {}
}