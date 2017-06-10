package com.stedi.randomimagegenerator.app.other;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class ChainSerializable implements Serializable {
    private ChainSerializable next;

    private final Serializable state;

    public ChainSerializable(@NonNull Serializable state) {
        this.state = state;
    }

    @NonNull
    public ChainSerializable createNext(@NonNull Serializable state) {
        next = new ChainSerializable(state);
        return next;
    }

    @Nullable
    public ChainSerializable getNext() {
        return next;
    }

    @NonNull
    public Serializable getState() {
        return state;
    }
}
