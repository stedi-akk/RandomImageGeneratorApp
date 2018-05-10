package com.stedi.randomimagegenerator.app;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.generators.Generator;

public class FailedCountGenerator implements Generator {
    private final Generator target;

    private int countToFail;
    private int generatedCount;

    public FailedCountGenerator(@NonNull Generator target) {
        this.target = target;
    }

    public void setToFail(int count) {
        countToFail = count;
    }

    public void reset() {
        generatedCount = 0;
    }

    @Override
    public Bitmap generate(ImageParams imageParams) throws Exception {
        if (generatedCount >= countToFail) {
            throw new Exception();
        }
        generatedCount++;
        return target.generate(imageParams);
    }
}
