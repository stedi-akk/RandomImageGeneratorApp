package com.stedi.randomimagegenerator.app.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.widget.Toast;

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

    @NonNull
    public static <T> List<T> sparseArrayToList(@NonNull SparseArray<T> array) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++)
            result.add(array.valueAt(i));
        return result;
    }

    public static void toastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
