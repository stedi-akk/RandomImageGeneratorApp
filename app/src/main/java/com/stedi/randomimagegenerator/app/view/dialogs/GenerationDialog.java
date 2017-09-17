package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.BaseDialogFragment;

import javax.inject.Inject;

public class GenerationDialog extends BaseDialogFragment implements GenerationPresenter.UIImpl {
    private static GenerationDialog instance;

    private ProgressDialog progressDialog;

    private State currentState = State.START;
    private int generatedCount;
    private int failedCount;

    private enum State {
        START,
        PROGRESS,
        FINISH,
        ERROR
    }

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.please_wait);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
        });
        setCancelable(false);
        return progressDialog;
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
    public void onGenerated(@NonNull ImageParams imageParams) {
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
                progressDialog.setTitle(R.string.please_wait);
                progressDialog.setMessage(getString(R.string.generating_image, String.valueOf(generatedCount + failedCount + 1)));
                progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
                break;
            case FINISH:
            case ERROR:
                progressDialog.setTitle(R.string.generation_results);
                if (currentState == State.FINISH) {
                    progressDialog.setMessage(getString(R.string.generated_stats, String.valueOf(generatedCount), String.valueOf(failedCount)));
                } else if (currentState == State.ERROR) {
                    progressDialog.setMessage(getString(R.string.generation_error));
                }
                progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                progressDialog.findViewById(android.R.id.progress).setVisibility(View.GONE);
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }
}
