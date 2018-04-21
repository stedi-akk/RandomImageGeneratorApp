package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.BaseDialogFragment
import javax.inject.Inject

class ConfirmDialog : BaseDialogFragment() {

    @Inject lateinit var bus: CachedBus

    private var requestCode: Int = 0
    private var posted: Boolean = false

    class Callback(val requestCode: Int, val confirm: Boolean)

    companion object {
        private const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_MESSAGE = "KEY_MESSAGE"

        fun newInstance(requestCode: Int, title: String?, message: String?): ConfirmDialog {
            return ConfirmDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_REQUEST_CODE, requestCode)
                    putString(KEY_TITLE, title)
                    putString(KEY_MESSAGE, message)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!).apply {
            requestCode = arguments?.getInt(KEY_REQUEST_CODE) ?: 0

            val title = arguments?.getString(KEY_TITLE, null)
            if (title != null) {
                setTitle(title)
            }

            val message = arguments?.getString(KEY_MESSAGE, null)
            if (message != null) {
                setMessage(message)
            }

            setPositiveButton(R.string.ok) { _, _ ->
                posted = true
                bus.postDeadEvent(Callback(requestCode, true))
            }
            setNegativeButton(R.string.cancel, null)
        }.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!posted) {
            bus.postDeadEvent(Callback(requestCode, false))
        }
    }
}