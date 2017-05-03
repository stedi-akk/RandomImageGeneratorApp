package com.stedi.randomimagegenerator.app.presenter;

import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

public interface GenerationPresenter extends RetainedPresenter<GenerationPresenter.UIImpl> {

    interface UIImpl extends UI {

    }
}
