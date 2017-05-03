package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.presenter.GenerationPresenter;
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerationActivity extends BaseActivity implements GenerationPresenter.UIImpl {
    @BindView(R.id.generation_activity_stepper) StepperLayout stepper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generation_activity);
        ButterKnife.bind(this);
        stepper.setAdapter(new GenerationStepperAdapter(getSupportFragmentManager(), this));
    }
}
