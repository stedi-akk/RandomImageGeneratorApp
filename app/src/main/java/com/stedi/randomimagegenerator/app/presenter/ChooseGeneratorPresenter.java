package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;
import com.stedi.randomimagegenerator.generators.Generator;

import java.util.List;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGenerators();

    void chooseGenerator(@NonNull Generator generator);

    interface UIImpl extends UI {
        void onShowGeneratorsToChoose(@NonNull List<Generator> generators);

        void onGeneratorChose(@NonNull Generator generator);
    }
}
