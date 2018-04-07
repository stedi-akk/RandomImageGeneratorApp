package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import android.widget.EditText
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import javax.inject.Inject

class EditPresetNameDialog : ButterKnifeDialogFragment() {

    @BindView(R.id.edit_preset_name_dialog_et_name)
    lateinit var etName: EditText

    @Inject
    lateinit var bus: CachedBus

    class OnEdited(val name: String)

    companion object {
        private val KEY_PRESET_NAME = "KEY_PRESET_NAME"

        fun newInstance(presetName: String): EditPresetNameDialog {
            return EditPresetNameDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_PRESET_NAME, presetName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.apply { getApp().component.inject(this@EditPresetNameDialog) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context as Context
        return AlertDialog.Builder(context).let { builder ->
            builder.setTitle(R.string.set_name)
            builder.setView(inflateAndBind(R.layout.edit_preset_name_dialog))
            builder.setPositiveButton(R.string.ok, null)

            val presetName = arguments?.getString(KEY_PRESET_NAME, "") ?: ""
            etName.setText(presetName)
            etName.setSelection(presetName.length)

            builder.create().apply {
                if (presetName.isEmpty()) {
                    etName.requestFocus()
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                }
                setOnShowListener { getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { apply() } }
            }
        }
    }

    private fun apply() {
        val name = etName.text.toString().trim { it <= ' ' }
        if (name.isEmpty()) {
            etName.error = getString(R.string.preset_name_empty)
            return
        }
        bus.postDeadEvent(OnEdited(name))
        dismiss()
    }
}