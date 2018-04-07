package com.stedi.randomimagegenerator.app.view.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import butterknife.BindView
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import java.io.File
import javax.inject.Inject

class GenerationDialog : ButterKnifeDialogFragment(), GenerationPresenter.UIImpl {
    private val KEY_CURRENT_STATE = "KEY_CURRENT_STATE"
    private val KEY_GENERATED_COUNT = "KEY_GENERATED_COUNT"
    private val KEY_FAILED_COUNT = "KEY_FAILED_COUNT"

    @BindView(R.id.generation_dialog_progress) lateinit var progressBar: View
    @BindView(R.id.generation_dialog_message) lateinit var tvMessage: TextView

    private var currentState = State.START
    private var generatedCount: Int = 0
    private var failedCount: Int = 0

    private enum class State {
        START,
        PROGRESS,
        FINISH,
        ERROR
    }

    class Dismissed

    @Inject lateinit var bus: CachedBus
    @Inject lateinit var logger: Logger

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: GenerationDialog? = null

        fun getInstance(fm: FragmentManager): GenerationDialog {
            if (instance == null) {
                instance = GenerationDialog().apply { show(fm) }
            }
            return instance!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.apply { getApp().component.inject(this@GenerationDialog) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (instance == null) {
            logger.log(this, "handling process kill")
            savedInstanceState?.apply {
                currentState = getSerializable(KEY_CURRENT_STATE) as State
                generatedCount = getInt(KEY_GENERATED_COUNT)
                failedCount = getInt(KEY_FAILED_COUNT)
            }
            if (currentState != State.FINISH) {
                currentState = State.ERROR
            }
        }

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        isCancelable = false

        val context = context as Context
        return AlertDialog.Builder(context).apply {
            setTitle(R.string.please_wait)
            setView(inflateAndBind(R.layout.generation_dialog))
            setPositiveButton(R.string.ok, null)
        }.create().apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnShowListener {
                getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    bus.postDeadEvent(Dismissed())
                    dismiss()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_CURRENT_STATE, currentState)
        outState.putInt(KEY_GENERATED_COUNT, generatedCount)
        outState.putInt(KEY_FAILED_COUNT, failedCount)
    }

    override fun onStartGeneration() {
        changeStateTo(State.START)
    }

    override fun onGenerated(imageParams: ImageParams, imageFile: File) {
        generatedCount++
        changeStateTo(State.PROGRESS)
    }

    override fun onGenerationUnknownError() {
        changeStateTo(State.ERROR)
    }

    override fun onFailedToGenerate(imageParams: ImageParams) {
        failedCount++
        changeStateTo(State.PROGRESS)
    }

    override fun onFinishGeneration() {
        changeStateTo(State.FINISH)
    }

    private fun changeStateTo(state: State) {
        if (currentState == State.ERROR || currentState == State.FINISH) {
            logger.log(this, "ignoring passed state because the end state is achieved (currentState=$currentState)")
            return
        }

        currentState = state

        if (isAdded) {
            invalidate()
        }
    }

    private fun invalidate() {
        when (currentState) {
            State.START, State.PROGRESS -> {
                invalidateCountMessage();
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).visibility = View.GONE
            }
            State.ERROR, State.FINISH -> {
                if (currentState == State.ERROR) {
                    showErrorMessage()
                } else if (currentState == State.FINISH) {
                    showFinishMessage()
                }
                dialog.setTitle(R.string.generation_results)
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun invalidateCountMessage() {
        tvMessage.text = getString(R.string.generating_image_s, (generatedCount + failedCount + 1).toString())
    }

    private fun showErrorMessage() {
        tvMessage.setText(R.string.generation_error)
    }

    private fun showFinishMessage() {
        tvMessage.text = getString(R.string.generated_stats, generatedCount.toString(), failedCount.toString())
    }
}