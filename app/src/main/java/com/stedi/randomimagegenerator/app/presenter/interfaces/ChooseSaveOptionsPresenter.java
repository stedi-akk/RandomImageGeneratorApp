package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ChooseSaveOptionsPresenter extends Presenter<ChooseSaveOptionsPresenter.UIImpl> {
    void setQualityFormat(@NonNull Bitmap.CompressFormat format);

    void setQualityValue(int value);

    void getData();

    interface UIImpl extends UI {
        void showQualityFormat(@NonNull Bitmap.CompressFormat format);

        void showQualityValue(int value);

        void showSaveFolder(@NonNull String path);

        void onIncorrectQualityValue();
    }
}
