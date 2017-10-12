package com.stedi.randomimagegenerator.app.view.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;

public class GenerationDialog extends ButterKnifeDialogFragment implements GenerationPresenter.UIImpl {
    @SuppressLint("StaticFieldLeak")
    private static GenerationDialog instance;

    private AlertDialog dialog;
    @BindView(R.id.generation_dialog_progress) View progressBar;
    @BindView(R.id.generation_dialog_message) TextView tvMessage;

    private State currentState = State.START;
    private int generatedCount;
    private int failedCount;

    private enum State {
        START,
        PROGRESS,
        FINISH,
        ERROR
    }

    public static class OkClicked {
    }

    @Inject CachedBus bus;
    @Inject Logger logger;

    @NonNull
    public static GenerationDialog getInstance(@NonNull FragmentManager fm) {
        if (instance == null) {
            instance = new GenerationDialog();
            instance.show(fm);
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getAppComponent(getContext()).inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (instance == null) {
            logger.log(this, "dismissing non instance dialog");
            dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.please_wait);
        builder.setView(inflateAndBind(R.layout.generation_dialog));
        builder.setPositiveButton(R.string.ok, null);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(d -> dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            bus.postDead(new OkClicked());
            dismiss();
        }));
        setCancelable(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onStartGeneration() {
        changeStateTo(State.START);
    }

    @Override
    public void onGenerated(@NonNull ImageParams imageParams, @NonNull File imageFile) {
        generatedCount++;
        changeStateTo(State.PROGRESS);
    }

    @Override
    public void onGenerationUnknownError() {
        changeStateTo(State.ERROR);
    }

    @Override
    public void onFailedToGenerate(@NonNull ImageParams imageParams) {
        failedCount++;
        changeStateTo(State.PROGRESS);
    }

    @Override
    public void onFinishGeneration() {
        changeStateTo(State.FINISH);
    }

    private void changeStateTo(State state) {
        if (currentState == State.ERROR || currentState.ordinal() > state.ordinal())
            throw new IllegalStateException("incorrect behavior");
        currentState = state;
        if (isAdded())
            invalidate();
    }

    private void invalidate() {
        logger.log(this, "invalidate state " + currentState);
        switch (currentState) {
            case START:
            case PROGRESS:
                dialog.setTitle(R.string.please_wait);
                tvMessage.setText(getString(R.string.generating_image_s, String.valueOf(generatedCount + failedCount + 1)));
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
                break;
            case FINISH:
            case ERROR:
                dialog.setTitle(R.string.generation_results);
                if (currentState == State.FINISH) {
                    tvMessage.setText(getString(R.string.generated_stats, String.valueOf(generatedCount), String.valueOf(failedCount)));
                } else if (currentState == State.ERROR) {
                    tvMessage.setText(R.string.generation_error);
                }
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }
}
