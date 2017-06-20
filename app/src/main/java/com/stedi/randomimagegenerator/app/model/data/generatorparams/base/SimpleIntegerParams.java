package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

public abstract class SimpleIntegerParams extends GeneratorParams {
    private Integer value;

    public SimpleIntegerParams() {
        if (!canBeRandom())
            value = 1;
    }

    public Integer getValue() {
        return value;
    }

    public void setRandomValue() {
        if (canBeRandom())
            this.value = null;
    }

    public void setValue(int value) {
        if (value < 1)
            throw new IllegalArgumentException("value must be > 0");
        this.value = value;
    }

    public abstract boolean canBeRandom();

    @Override
    public boolean isEditable() {
        return true;
    }
}
