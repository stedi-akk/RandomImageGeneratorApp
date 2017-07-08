package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class EditPresetNameDialog extends ButterKnifeDialogFragment {
    private static final String KEY_PRESET_NAME = "KEY_PRESET_NAME";

    @BindView(R.id.edit_preset_name_dialog_et_name) EditText etName;

    @Inject CachedBus bus;

    public static class OnEdited {
        public final String name;

        OnEdited(String name) {
            this.name = name;
        }
    }

    public static EditPresetNameDialog newInstance(String presetName) {
        Bundle args = new Bundle();
        args.putString(KEY_PRESET_NAME, presetName);
        EditPresetNameDialog dlg = new EditPresetNameDialog();
        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getAppComponent(getContext()).inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String fromName = getArguments().getString(KEY_PRESET_NAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit name");
        builder.setView(inflateAndBind(R.layout.edit_preset_name_dialog));
        etName.setText(fromName);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> apply());
        });
        return dialog;
    }

    private void apply() {
        String name = etName.getText().toString();

        if (name.isEmpty()) {
            etName.setError("empty");
            return;
        }

        bus.post(new OnEdited(name));
        dismiss();
    }
}
