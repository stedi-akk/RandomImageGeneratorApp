package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.ExtKt;
import com.stedi.randomimagegenerator.app.view.dialogs.base.BaseDialogFragment;

import javax.inject.Inject;

public class ConfirmDialog extends BaseDialogFragment {
    private static final String KEY_REQUEST_CODE = "KEY_REQUEST_CODE";
    private static final String KEY_TITLE = "KEY_TITLE";
    private static final String KEY_MESSAGE = "KEY_MESSAGE";

    @Inject CachedBus bus;

    private int requestCode;
    private boolean posted;

    public static class Callback {
        public final int requestCode;
        public final boolean confirm;

        Callback(int requestCode, boolean confirm) {
            this.requestCode = requestCode;
            this.confirm = confirm;
        }
    }

    @NonNull
    public static ConfirmDialog newInstance(int requestCode, @Nullable String title, @Nullable String message) {
        Bundle args = new Bundle();
        args.putInt(KEY_REQUEST_CODE, requestCode);
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        ConfirmDialog dlg = new ConfirmDialog();
        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExtKt.getApp(getContext()).getComponent().inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        requestCode = getArguments().getInt(KEY_REQUEST_CODE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = getArguments().getString(KEY_TITLE, null);
        if (title != null)
            builder.setTitle(title);
        String message = getArguments().getString(KEY_MESSAGE, null);
        if (message != null)
            builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            posted = true;
            bus.postDead(new Callback(requestCode, true));
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!posted)
            bus.postDead(new Callback(requestCode, false));
    }
}
