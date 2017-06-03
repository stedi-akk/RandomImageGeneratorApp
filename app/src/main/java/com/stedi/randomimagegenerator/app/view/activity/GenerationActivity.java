package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent;
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter;
import com.stepstone.stepper.StepperLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerationActivity extends BaseActivity implements GenerationPresenter.UIImpl {
    @Inject GenerationPresenter presenter;

    @BindView(R.id.generation_activity_stepper) StepperLayout stepper;

    private GenerationComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = getActivityComponent().plus(new GenerationModule());
        component.inject(this);
        presenter.onAttach(this);
        setContentView(R.layout.generation_activity);
        ButterKnife.bind(this);
        stepper.setAdapter(new GenerationStepperAdapter(getSupportFragmentManager(), this));
    }

    @NonNull
    public GenerationComponent getGenerationComponent() {
        return component;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public boolean canRetain() {
        return false;
    }
}
