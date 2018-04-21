package com.stedi.randomimagegenerator.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter
import com.stepstone.stepper.StepperLayout

class GenerationStepsActivity : BaseActivity() {
    private val KEY_CURRENT_STEP = "KEY_CURRENT_STEP"

    @BindView(R.id.generation_steps_activity_stepper) lateinit var stepper: StepperLayout

    companion object {
        private const val KEY_NEW_GENERATION = "KEY_NEW_GENERATION"

        fun start(context: Context, newGeneration: Boolean) {
            val intent = Intent(context, GenerationStepsActivity::class.java)
            intent.putExtra(KEY_NEW_GENERATION, newGeneration)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.generation_steps_activity)
        ButterKnife.bind(this)

        val stepper = findViewById<StepperLayout>(R.id.generation_steps_activity_stepper)
        val stepperAdapter = GenerationStepperAdapter(supportFragmentManager, this)
        stepper.adapter = stepperAdapter

        val isNew = intent.getBooleanExtra(KEY_NEW_GENERATION, true)
        if (savedInstanceState != null) {
            stepper.currentStepPosition = savedInstanceState.getInt(KEY_CURRENT_STEP)
        } else {
            stepper.currentStepPosition = if (isNew) 0 else stepperAdapter.count - 1
        }

        setTitle(if (isNew) R.string.new_preset else R.string.preset_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_STEP, stepper.currentStepPosition)
    }
}