package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.annotation.WorkerThread
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GenerationDialog : ButterKnifeDialogFragment(), GenerationPresenter.UIImpl {
    private val KEY_TARGET_COUNT = "KEY_TARGET_COUNT"
    private val KEY_CURRENT_STATE = "KEY_CURRENT_STATE"
    private val KEY_GENERATED_COUNT = "KEY_GENERATED_COUNT"
    private val KEY_FAILED_COUNT = "KEY_FAILED_COUNT"

    @BindView(R.id.generation_dialog_progress) lateinit var progressBar: View
    @BindView(R.id.generation_dialog_message) lateinit var tvMessage: TextView
    @BindView(R.id.generation_dialog_result_icon) lateinit var ivResult: ImageView

    private var targetCount: Int = 0
    private var currentState = State.PROGRESS
    private var generatedCount: Int = 0
    private var failedCount: Int = 0

    private enum class State {
        PROGRESS,
        FINISH,
        ERROR
    }

    class OnDismissed

    @Inject lateinit var presenter: GenerationPresenter
    @Inject lateinit var bus: LockedBus
    @Inject @field:AppContext lateinit var appContext: Context

    private var isDialogStarted: Boolean = false

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
        (activity as BaseActivity).activityComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            if (!isDialogStarted) {
                targetCount = savedInstanceState.getInt(KEY_TARGET_COUNT)
                currentState = savedInstanceState.getSerializable(KEY_CURRENT_STATE) as State
                generatedCount = savedInstanceState.getInt(KEY_GENERATED_COUNT)
                failedCount = savedInstanceState.getInt(KEY_FAILED_COUNT)
                if (currentState != State.FINISH) {
                    currentState = State.ERROR
                }
            }
        } else {
            val arguments = arguments
            if (arguments != null) {
                val preset: Preset = arguments.getParcelable(KEY_PRESET)
                targetCount = preset.getRealCount()
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
                    bus.post(OnDismissed())
                    dismiss()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        isDialogStarted = true
        invalidateViews()
    }

    @WorkerThread
    override fun imageSaved(bitmap: Bitmap, file: File) {
        appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }

    override fun onResult(generatedCount: Int, failedCount: Int) {
        this.generatedCount = generatedCount
        this.failedCount = failedCount
        invalidateState(State.PROGRESS)
    }

    override fun onFinishGeneration() {
        invalidateState(State.FINISH)
    }

    override fun onGenerationFailed() {
        invalidateState(State.ERROR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        presenter.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TARGET_COUNT, targetCount)
        outState.putSerializable(KEY_CURRENT_STATE, currentState)
        outState.putInt(KEY_GENERATED_COUNT, generatedCount)
        outState.putInt(KEY_FAILED_COUNT, failedCount)
    }

    private fun invalidateState(state: State) {
        if (currentState == State.ERROR || currentState == State.FINISH) {
            Timber.e("ignoring passed state because the end state is achieved")
            return
        }

        currentState = state

        if (isDialogStarted) {
            invalidateViews()
        }
    }

    private fun invalidateViews() {
        when (currentState) {
            State.PROGRESS -> {
                if (targetCount > 1) {
                    val generatingN = (generatedCount + failedCount + 1).let { if (it > targetCount) targetCount else it }
                    tvMessage.text = getString(R.string.generating_image_s_from_s, generatingN.toString(), targetCount.toString())
                } else {
                    tvMessage.text = getString(R.string.generating_image)
                }
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).visibility = View.GONE
            }
            State.ERROR, State.FINISH -> {
                if (currentState == State.ERROR) {
                    tvMessage.setText(R.string.generation_error)
                    ivResult.setImageResource(R.drawable.ic_fail)
                } else if (currentState == State.FINISH) {
                    tvMessage.text = getString(R.string.generated_stats, generatedCount.toString(), failedCount.toString())
                    ivResult.setImageResource(if (failedCount > 0) R.drawable.ic_warning else R.drawable.ic_success)
                }
                dialog.setTitle(R.string.generation_results)
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                ivResult.visibility = View.VISIBLE
            }
        }
    }
}