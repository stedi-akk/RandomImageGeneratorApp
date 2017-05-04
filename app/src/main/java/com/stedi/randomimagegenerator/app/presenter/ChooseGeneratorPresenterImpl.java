package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class ChooseGeneratorPresenterImpl implements ChooseGeneratorPresenter {
    private UIImpl ui;

    @Override
    public void getGenerators() {

    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }

    @Override
    public void onRestore(@NonNull Serializable state) {

    }

    @Nullable
    @Override
    public Serializable onRetain() {
        return null;
    }
}
