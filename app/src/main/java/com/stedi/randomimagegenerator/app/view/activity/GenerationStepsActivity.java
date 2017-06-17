package com.stedi.randomimagegenerator.app.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent;
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerationStepsActivity extends BaseActivity implements
        GenerationStepsPresenter.UIImpl,
        StepperLayout.StepperListener {

    private static final String KEY_NEW_GENERATION = "KEY_NEW_GENERATION";
    private static final String KEY_CURRENT_STEP = "KEY_CURRENT_STEP";
    private static final String KEY_GENERATION_STEPS_PRESENTER_STATE = "KEY_GENERATION_STEPS_PRESENTER_STATE";
    private static final int REQUEST_CODE_WRITE_EXTERNAL = 22;

    private GenerationComponent component;

    @Inject GenerationStepsPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.generation_steps_activity_stepper) StepperLayout stepper;

    private GenerationStepperAdapter stepperAdapter;

    public static void startActivity(Context context, boolean newGeneration) {
        Intent intent = new Intent(context, GenerationStepsActivity.class);
        intent.putExtra(KEY_NEW_GENERATION, newGeneration);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        component = getActivityComponent().plus(new GenerationModule());
        component.inject(this);
        super.onCreate(savedInstanceState);
        presenter.onAttach(this);

        setContentView(R.layout.generation_steps_activity);
        ButterKnife.bind(this);

        stepperAdapter = new GenerationStepperAdapter(getSupportFragmentManager(), this);
        stepper.setAdapter(stepperAdapter);
        stepper.setListener(this);

        if (savedInstanceState == null) {
            presenter.setIsNew(getIntent().getBooleanExtra(KEY_NEW_GENERATION, true));
        } else {
            Serializable state = savedInstanceState.getSerializable(KEY_GENERATION_STEPS_PRESENTER_STATE);
            if (state != null)
                presenter.onRestore(state);
            stepper.setCurrentStepPosition(savedInstanceState.getInt(KEY_CURRENT_STEP));
        }
    }

    @NonNull
    public GenerationComponent getGenerationComponent() {
        return component;
    }

    @Override
    public void showFirstStep() {
        stepper.setCurrentStepPosition(0);
    }

    @Override
    public void showFinishStep() {
        stepper.setCurrentStepPosition(stepperAdapter.getCount() - 1);
    }

    @Override
    public void onCompleted(View completeButton) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL))
            presenter.startGeneration(presenter.getCandidate());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_GENERATION_STEPS_PRESENTER_STATE, presenter.onRetain());
        outState.putInt(KEY_CURRENT_STEP, stepper.getCurrentStepPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing())
            presenter.release();
        presenter.onDetach();
    }

    @Override
    public void onStartGeneration() {
        logger.log(this, "onStartGeneration");
    }

    @Override
    public void onGenerated(@NonNull ImageParams imageParams) {
        logger.log(this, "onGenerated");
    }

    @Override
    public void onGenerationUnknownError() {
        logger.log(this, "onGenerationUnknownError");
    }

    @Override
    public void onFailedToGenerate(@NonNull ImageParams imageParams) {
        logger.log(this, "onFailedToGenerate");
    }

    @Override
    public void onFinishGeneration() {
        Utils.toastShort(this, getClass().getSimpleName() + " onFinishGeneration");
    }

    @Override
    public void onError(VerificationError verificationError) {
    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onReturn() {
    }
}
