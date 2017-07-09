package com.stedi.randomimagegenerator.app.model.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.generators.Generator;

public class SlowGenerator implements Generator {
    private final Generator target;

    public SlowGenerator(@NonNull Generator target) {
        this.target = target;
    }

    @Override
    public Bitmap generate(ImageParams imageParams) throws Exception {
        Utils.sleep(1000);
        return target.generate(imageParams);
    }
}
