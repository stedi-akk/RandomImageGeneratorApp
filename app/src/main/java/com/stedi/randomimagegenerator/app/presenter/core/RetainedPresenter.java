package com.stedi.randomimagegenerator.app.presenter.core;

import java.io.Serializable;

public interface RetainedPresenter<T extends UI> extends Presenter<T> {
    void onRestore(Serializable state);

    void onRetain(Serializable state);
}
