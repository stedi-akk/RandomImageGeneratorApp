package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

public interface ChooseSaveOptionsPresenter extends RetainedPresenter<ChooseSaveOptionsPresenter.UIImpl> {
    void chooseQualityFormat(@NonNull Bitmap.CompressFormat format);

    void chooseQualityValue(int value);

    void chooseSaveFolder(String path);

    void getFormatsToChoose();

    void getValuesToChoose();

    void getSaveFolder();

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void showQualityFormats(@NonNull Bitmap.CompressFormat[] formats);

        void showQualityValueRange(@NonNull int[] minMax);

        void showSaveFolder(@NonNull String path);
    }
}
