package com.stedi.randomimagegenerator.app.presenter;

import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;
import com.stedi.randomimagegenerator.generators.Generator;

import java.util.List;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGenerators();

    interface UIImpl extends UI {
        void onShowGeneratorsToChoose(List<Generator> generators);
    }
}
