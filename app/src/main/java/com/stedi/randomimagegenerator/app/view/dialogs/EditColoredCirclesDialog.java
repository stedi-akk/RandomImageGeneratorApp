package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.presenter.interfaces.EditColoredCirclesPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;

import javax.inject.Inject;

public class EditColoredCirclesDialog extends ButterKnifeDialogFragment implements EditColoredCirclesPresenter.UIImpl {
    @Inject EditColoredCirclesPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", (dialog, which) -> {
        });
        builder.setNegativeButton("CANCEL", null);
        builder.setTitle("Edit colored circles");
        builder.setView(inflateAndBind(R.layout.edit_colored_circles_dialog));
        return builder.create();
    }

    @Override
    public void showRandomCount() {

    }

    @Override
    public void showCount(int count) {

    }

    @Override
    public void showErrorIncorrectCount() {

    }

    @Override
    public void finishView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
