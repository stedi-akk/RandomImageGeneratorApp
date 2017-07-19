package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.DefaultFileNamePolicy;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.TextOverlayGenerator;

@DatabaseTable(tableName = "text_overlay_params")
public class TextOverlayParams extends EffectGeneratorParams {
    public TextOverlayParams() {
    }

    public TextOverlayParams(@NonNull GeneratorParams target) {
        super(target);
    }

    @NonNull
    @Override
    protected Generator createEffectGenerator(@NonNull Generator target) {
        return new TextOverlayGenerator.Builder()
                .setGenerator(target)
                .setTextPolicy(imageParams -> new DefaultFileNamePolicy().getName(imageParams))
                .build();
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.TEXT_OVERLAY;
    }

    protected TextOverlayParams(Parcel in) {
        super(in);
    }

    public static final Creator<TextOverlayParams> CREATOR = new Creator<TextOverlayParams>() {
        @Override
        public TextOverlayParams createFromParcel(Parcel source) {
            return new TextOverlayParams(source);
        }

        @Override
        public TextOverlayParams[] newArray(int size) {
            return new TextOverlayParams[size];
        }
    };
}
