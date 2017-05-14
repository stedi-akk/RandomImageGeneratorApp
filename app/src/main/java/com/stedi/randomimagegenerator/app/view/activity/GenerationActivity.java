package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent;
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerationActivity extends BaseActivity {
    @BindView(R.id.generation_activity_stepper) StepperLayout stepper;

    private GenerationComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generation_activity);
        ButterKnife.bind(this);
        stepper.setAdapter(new GenerationStepperAdapter(getSupportFragmentManager(), this));
    }

    @NonNull
    public GenerationComponent getGenerationComponent() {
        if (component == null)
            component = getActivityComponent().plus(new GenerationModule());
        return component;
    }
}
