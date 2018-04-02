package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.dialogs.base.BaseDialogFragment
import javax.inject.Inject

class ConfirmDialog : BaseDialogFragment() {

    @Inject
    lateinit var bus: CachedBus

    private var requestCode: Int = 0
    private var posted: Boolean = false

    class Callback(val requestCode: Int, val confirm: Boolean)

    companion object {
        private val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"
        private val KEY_TITLE = "KEY_TITLE"
        private val KEY_MESSAGE = "KEY_MESSAGE"

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
        context?.apply { getApp().component.inject(this@ConfirmDialog) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context as Context
        return AlertDialog.Builder(context).let {
            requestCode = arguments?.getInt(KEY_REQUEST_CODE) ?: 0

            val title = arguments?.getString(KEY_TITLE, null)
            if (title != null) {
                it.setTitle(title)
            }

            val message = arguments?.getString(KEY_MESSAGE, null)
            if (message != null) {
                it.setMessage(message)
            }

            it.setPositiveButton(R.string.ok) { _, _ ->
                posted = true
                bus.postDeadEvent(Callback(requestCode, true))
            }
            it.setNegativeButton(R.string.cancel, null)
            it.create()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!posted) {
            bus.postDeadEvent(Callback(requestCode, false))
        }
    }
}