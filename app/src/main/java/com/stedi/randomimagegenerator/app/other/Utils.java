package com.stedi.randomimagegenerator.app.other;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private Utils() {
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> sparseArrayToList(SparseArray<T> array) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++)
            result.add(array.valueAt(i));
        return result;
    }
}
