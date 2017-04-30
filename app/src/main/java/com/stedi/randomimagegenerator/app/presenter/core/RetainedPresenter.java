package com.stedi.randomimagegenerator.app.presenter.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public interface RetainedPresenter<T extends UI> extends Presenter<T> {
    void onRestore(@NonNull Serializable state);

    @Nullable
    Serializable onRetain();
}
