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
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapter;
import com.stedi.randomimagegenerator.app.view.adapters.GeneratorTypeAdapterImageLoader;
import com.stedi.randomimagegenerator.app.view.dialogs.SimpleIntegerParamsDialog;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseGeneratorFragment extends StepFragment implements
        ChooseGeneratorPresenter.UIImpl,
        GeneratorTypeAdapter.ClickListener {

    @Inject ChooseGeneratorPresenter presenter;
    @Inject Logger logger;
    @Inject GeneratorTypeAdapterImageLoader adapterImageLoader;

    @BindView(R.id.choose_generator_fragment_recycler_view) RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.choose_generator_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        presenter.getGeneratorTypes();
    }

    @Override
    public void showTypes(@NonNull GeneratorType[] types, @NonNull GeneratorType selectedType) {
        recyclerView.setAdapter(new GeneratorTypeAdapter(adapterImageLoader, types, selectedType, this));
    }

    @Override
    public void onSelected(@NonNull GeneratorType type) {
        presenter.chooseGeneratorType(type);
    }

    @Override
    public void onEditSelected() {
        presenter.editChoseGeneratorParams();
    }

    @Override
    public void showEditGeneratorParams(@NonNull GeneratorType type) {
        switch (type) {
            case COLORED_PIXELS:
            case COLORED_CIRCLES:
            case COLORED_RECTANGLE:
                SimpleIntegerParamsDialog.newInstance(type).show(getFragmentManager(), SimpleIntegerParamsDialog.class.getSimpleName());
                break;
            case COLORED_NOISE:
                throw new IllegalStateException("todo");
            default:
                throw new IllegalStateException("incorrect behavior");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
