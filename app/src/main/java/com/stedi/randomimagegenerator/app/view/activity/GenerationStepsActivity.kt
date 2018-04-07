package com.stedi.randomimagegenerator.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.adapters.GenerationStepperAdapter
import com.stepstone.stepper.StepperLayout
import javax.inject.Inject

class GenerationStepsActivity : BaseActivity(), GenerationStepsPresenter.UIImpl {
    private val KEY_CURRENT_STEP = "KEY_CURRENT_STEP"

    lateinit var generationComponent: GenerationComponent

    @Inject lateinit var presenter: GenerationStepsPresenter
    @Inject lateinit var logger: Logger

    @BindView(R.id.generation_steps_activity_stepper) lateinit var stepper: StepperLayout

    private lateinit var stepperAdapter: GenerationStepperAdapter

    companion object {
        private const val KEY_NEW_GENERATION = "KEY_NEW_GENERATION"

        fun start(context: Context, newGeneration: Boolean) {
            val intent = Intent(context, GenerationStepsActivity::class.java)
            intent.putExtra(KEY_NEW_GENERATION, newGeneration)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        generationComponent = component.plus(GenerationModule())
        generationComponent.inject(this)
        super.onCreate(savedInstanceState)
        presenter.onAttach(this)

        setContentView(R.layout.generation_steps_activity)
        ButterKnife.bind(this)

        stepperAdapter = GenerationStepperAdapter(supportFragmentManager, this)
        stepper.adapter = stepperAdapter

        val isNew = intent.getBooleanExtra(KEY_NEW_GENERATION, true)
        savedInstanceState?.apply {
            stepper.currentStepPosition = getInt(KEY_CURRENT_STEP)
        } ?: presenter.setIsNew(isNew)

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

    override fun showFirstStep() {
        stepper.currentStepPosition = 0
    }

    override fun showFinishStep() {
        stepper.currentStepPosition = stepperAdapter.count - 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_STEP, stepper.currentStepPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            presenter.release()
        }
        presenter.onDetach()
    }
}