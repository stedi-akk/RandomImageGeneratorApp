package com.stedi.randomimagegenerator.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent;
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter;
import com.stepstone.stepper.StepperLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerationStepsActivity extends BaseActivity implements GenerationStepsPresenter.UIImpl {
    private static final String KEY_NEW_GENERATION = "KEY_NEW_GENERATION";
    private static final String KEY_CURRENT_STEP = "KEY_CURRENT_STEP";

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

        boolean isNew = getIntent().getBooleanExtra(KEY_NEW_GENERATION, true);
        if (savedInstanceState == null) {
            presenter.setIsNew(isNew);
        } else {
            stepper.setCurrentStepPosition(savedInstanceState.getInt(KEY_CURRENT_STEP));
        }

        setTitle(isNew ? R.string.new_preset : R.string.preset_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_STEP, stepper.getCurrentStepPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing())
            presenter.release();
        presenter.onDetach();
    }
}
