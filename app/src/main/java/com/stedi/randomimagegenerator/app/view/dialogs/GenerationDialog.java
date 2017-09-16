package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.BaseDialogFragment;

public class GenerationDialog extends BaseDialogFragment implements GenerationPresenter.UIImpl {
    private static GenerationDialog instance;

    private ProgressDialog progressDialog;

    private State currentState = State.START;
    private int generatedCount;
    private int failedCount;

    private enum State {
        START,
        PROGRESS,
        FINISH
    }

    @NonNull
    public static GenerationDialog getInstance(@NonNull FragmentManager fm) {
        if (instance == null) {
            instance = new GenerationDialog();
            instance.show(fm);
        }
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
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
        changeStateTo(State.FINISH);
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
        if (currentState.ordinal() > state.ordinal())
            throw new IllegalStateException("incorrect behavior");
        currentState = state;
        if (isAdded())
            invalidate();
    }

    private void invalidate() {
        switch (currentState) {
            case START:
                progressDialog.setTitle(R.string.please_wait);
                progressDialog.setMessage(getString(R.string.generating));
                progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
                break;
            case PROGRESS:
                progressDialog.setTitle(R.string.please_wait);
                progressDialog.setMessage(getString(R.string.generating));
                progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
                break;
            case FINISH:
                progressDialog.setTitle(R.string.generation_results);
                progressDialog.setMessage(getString(R.string.generated_stats, String.valueOf(generatedCount), String.valueOf(failedCount)));
                progressDialog.findViewById(android.R.id.progress).setVisibility(View.GONE);
                progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }
}
