package com.stedi.randomimagegenerator.app.presenter.core;

import android.support.annotation.NonNull;

public interface Presenter<T extends UI> {
    void onAttach(@NonNull T ui);

    void onDetach();
}
