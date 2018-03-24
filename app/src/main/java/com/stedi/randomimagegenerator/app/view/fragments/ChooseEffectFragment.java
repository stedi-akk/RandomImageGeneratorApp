package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseEffectFragment extends StepFragment implements
        GeneratorTypeAdapter.ClickListener,
        ChooseEffectPresenter.UIImpl {

    @Inject ChooseEffectPresenter presenter;
    @Inject Logger logger;
    @Inject GeneratorTypeImageLoader adapterImageLoader;

    @BindView(R.id.choose_effect_fragment_recycler_view) RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GenerationStepsActivity) getActivity()).getGenerationComponent().inject(this);
        presenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.choose_effect_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.generator_type_adapter_grid_items)));
        recyclerView.setHasFixedSize(true);
        presenter.getEffectTypes();
    }

    @Override
    public void onSelected() {
        if (getView() != null) {
            presenter.getEffectTypes();
        }
    }

    @Override
    public void showTypes(@NonNull GeneratorType[] types, @Nullable GeneratorType selectedType, @NonNull GeneratorType targetType) {
        recyclerView.setAdapter(new GeneratorTypeAdapter(adapterImageLoader, types, selectedType, targetType, this, true));
    }

    @Override
    public void onSelected(@NonNull GeneratorType type) {
        logger.log(this, "onSelected: " + type.name());
        presenter.chooseEffectType(type);
    }

    @Override
    public void onDeselected() {
        logger.log(this, "onDeselected");
        presenter.chooseEffectType(null);
    }

    @Override
    public void onEditSelected() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
