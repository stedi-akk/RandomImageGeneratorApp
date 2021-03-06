package com.stedi.randomimagegenerator.app.view.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import android.widget.EditText
import butterknife.BindView
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment
import javax.inject.Inject

class EditPresetNameDialog : ButterKnifeDialogFragment() {

    @BindView(R.id.edit_preset_name_dialog_et_name) lateinit var etName: EditText

    @Inject lateinit var bus: LockedBus

    class Callback(val name: String)

    companion object {
        private const val KEY_PRESET_NAME = "KEY_PRESET_NAME"

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
        (activity as BaseActivity).activityComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val presetName = arguments?.getString(KEY_PRESET_NAME, "") ?: ""

        return AlertDialog.Builder(context!!).apply {
            setTitle(R.string.set_name)
            setView(inflateAndBind(R.layout.edit_preset_name_dialog))
            etName.setText(presetName)
            etName.setSelection(presetName.length)
            setPositiveButton(R.string.ok, null)
        }.create().apply {
            if (presetName.isEmpty()) {
                etName.requestFocus()
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            }
            setOnShowListener { getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { applyName() } }
        }
    }

    private fun applyName() {
        val name = etName.text.toString().trim { it <= ' ' }
        if (name.isEmpty()) {
            etName.error = getString(R.string.preset_name_empty)
            return
        }
        bus.post(Callback(name))
        dismiss()
    }
}