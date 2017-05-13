package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.di.components.ChooseGeneratorComponent;
import com.stedi.randomimagegenerator.app.di.modules.ChooseGeneratorModule;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.view.adapters.ChooseGeneratorAdapter;
import com.stedi.randomimagegenerator.app.view.dialogs.EditColoredCirclesDialog;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseGeneratorFragment extends ButterKnifeFragment implements
        ChooseGeneratorPresenter.UIImpl,
        ChooseGeneratorAdapter.ClickListener,
        Step {

    @Inject ChooseGeneratorPresenter presenter;

    @BindView(R.id.choose_generator_fragment_recycler_view) RecyclerView recyclerView;

    private ChooseGeneratorComponent component;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChooseGeneratorComponent().inject(this);
        presenter.onAttach(this);
    }

    @NonNull
    public ChooseGeneratorComponent getChooseGeneratorComponent() {
        if (component == null)
            component = Components.getActivityComponent(this).plus(new ChooseGeneratorModule());
        return component;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void showTypesToChoose(@NonNull GeneratorType[] types) {
        recyclerView.setAdapter(new ChooseGeneratorAdapter(types, this));
    }

    @Override
    public void onSelected(@NonNull GeneratorType type) {
        Utils.toastShort(getContext(), "onSelected: " + type.name());
        presenter.chooseGeneratorType(type);
    }

    @Override
    public void onEditSelected() {
        Utils.toastShort(getContext(), "onEditSelected");
        presenter.editChoseGeneratorParams();
    }

    @Override
    public void showEditGeneratorParams(@NonNull GeneratorType type) {
        DialogFragment dialog;
        switch (type) {
            case COLORED_CIRCLES:
                dialog = new EditColoredCirclesDialog();
                break;
            case COLORED_NOISE:
                dialog = new EditColoredCirclesDialog();
                break;
            case COLORED_PIXELS:
                dialog = new EditColoredCirclesDialog();
                break;
            case COLORED_RECTANGLE:
                dialog = new EditColoredCirclesDialog();
                break;
            default:
                throw new IllegalStateException("incorrect behavior");
        }
        dialog.setTargetFragment(this, -1);
        dialog.show(getFragmentManager(), "test");
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
