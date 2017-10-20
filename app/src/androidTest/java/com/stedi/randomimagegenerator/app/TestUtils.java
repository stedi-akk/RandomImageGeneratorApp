package com.stedi.randomimagegenerator.app;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import java.io.File;
import java.io.IOException;

public final class TestUtils {
    private TestUtils() {
    }

    @NonNull
    public static File getTestFolder() {
        return new File(InstrumentationRegistry.getTargetContext().getApplicationInfo().dataDir, "tests");
    }

    public static void deleteTestFolder() {
        try {
            deleteRecursively(TestUtils.getTestFolder());
            System.out.println("test folder successfully deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static Preset newSimplePreset() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), TestUtils.getTestFolder().getAbsolutePath());
        preset.setTimestamp(System.currentTimeMillis());
        return preset;
    }

    private static void deleteRecursively(File fileOrDirectory) throws IOException {
        if (!fileOrDirectory.exists())
            return;

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursively(child);

        if (!fileOrDirectory.delete())
            throw new IOException("failed to delete: " + fileOrDirectory.getAbsolutePath());
    }
}
