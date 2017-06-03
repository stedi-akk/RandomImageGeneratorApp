package com.stedi.randomimagegenerator.app.presenter.interfaces.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public interface RetainedPresenter<T extends RetainedPresenter.RetainedUI> extends Presenter<T> {
    void onRestore(@NonNull Serializable state);

    @Nullable
    Serializable onRetain();

    interface RetainedUI extends UI {
        boolean canRetain();
    }
}
