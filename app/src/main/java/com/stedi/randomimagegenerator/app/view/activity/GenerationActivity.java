package com.stedi.randomimagegenerator.app.view.activity;

import android.content.Context;
import android.content.Intent;
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
    private static final String KEY_NEW_GENERATION = "KEY_NEW_GENERATION";

    private GenerationComponent component;

    @Inject GenerationPresenter presenter;

    @BindView(R.id.generation_activity_stepper) StepperLayout stepper;

    private GenerationStepperAdapter stepperAdapter;

    public static void startActivity(Context context, boolean newGeneration) {
        Intent intent = new Intent(context, GenerationActivity.class);
        intent.putExtra(KEY_NEW_GENERATION, newGeneration);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        component = getActivityComponent().plus(new GenerationModule());
        component.inject(this);
        super.onCreate(savedInstanceState);
        presenter.onAttach(this);

        setContentView(R.layout.generation_activity);
        ButterKnife.bind(this);

        stepperAdapter = new GenerationStepperAdapter(getSupportFragmentManager(), this);
        stepper.setAdapter(stepperAdapter);

        presenter.setIsNew(getIntent().getBooleanExtra(KEY_NEW_GENERATION, true));
    }

    @NonNull
    public GenerationComponent getGenerationComponent() {
        return component;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void showFirstStep() {
        stepper.setCurrentStepPosition(0);
    }

    @Override
    public void showLastStep() {
        stepper.setCurrentStepPosition(stepperAdapter.getCount() - 1);
    }
}
