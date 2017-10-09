package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.DefaultFileNamePolicy;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.TextOverlayGenerator;

@DatabaseTable(tableName = "text_overlay_params")
public class TextOverlayParams extends EffectGeneratorParams {
    @DatabaseField(generatedId = true)
    private int id;

    public TextOverlayParams() {
    }

    public TextOverlayParams(@NonNull GeneratorParams target) {
        super(target);
    }

    @NonNull
    @Override
    protected Generator createEffectGenerator(@NonNull Generator target) {
        DefaultFileNamePolicy fileNamePolicy = new DefaultFileNamePolicy();
        return new TextOverlayGenerator.Builder()
                .setGenerator(target)
                .setTextPolicy(fileNamePolicy::getName)
                .build();
    }

    @Override
    public int getId() {
        return id;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
    }

    protected TextOverlayParams(Parcel in) {
        super(in);
        this.id = in.readInt();
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
