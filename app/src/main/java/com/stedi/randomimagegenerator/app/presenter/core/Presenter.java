package com.stedi.randomimagegenerator.app.presenter.core;

public interface Presenter<T extends UI> {
    void onAttach(T ui);

    void onDetach();
}
