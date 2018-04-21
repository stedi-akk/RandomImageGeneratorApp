package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import butterknife.BindView
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import timber.log.Timber
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

    class OnDismissed

    @Inject lateinit var presenter: GenerationPresenter
    @Inject lateinit var bus: CachedBus

    private lateinit var appContext: Context

    private var isStarted: Boolean = false

    companion object {
        private const val KEY_PRESET = "KEY_PRESET"

        fun newInstance(preset: Preset): GenerationDialog {
            return GenerationDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_PRESET, preset)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = context!!.applicationContext
        (activity as BaseActivity).activityComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
//            currentState = savedInstanceState.getSerializable(KEY_CURRENT_STATE) as State
//            if (currentState != State.FINISH) {
//                currentState = State.ERROR
//            }
//            generatedCount = savedInstanceState.getInt(KEY_GENERATED_COUNT)
//            failedCount = savedInstanceState.getInt(KEY_FAILED_COUNT)
        } else {
            val arguments = arguments;
            if (arguments != null) {
                val preset: Preset = arguments.getParcelable(KEY_PRESET)
                presenter.startGeneration(preset)
            } else {
                currentState = State.ERROR
            }
        }

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        isCancelable = false

        return AlertDialog.Builder(context!!).apply {
            setTitle(R.string.please_wait)
            setView(inflateAndBind(R.layout.generation_dialog))
            setPositiveButton(R.string.ok, null)
        }.create().apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnShowListener {
                getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    bus.postDeadEvent(OnDismissed())
                    dismiss()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        isStarted = true
        invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
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
        appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)))
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
            Timber.e("ignoring passed state because the end state is achieved (currentState=$currentState)")
            return
        }

        currentState = state

        if (isStarted) {
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