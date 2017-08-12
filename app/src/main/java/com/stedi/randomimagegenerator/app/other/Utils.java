package com.stedi.randomimagegenerator.app.other;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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

    public static void hideInput(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void deleteRecursively(@NonNull File fileOrDirectory) throws IOException {
        if (!fileOrDirectory.exists())
            return;

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursively(child);

        if (!fileOrDirectory.delete())
            throw new IOException("failed to delete: " + fileOrDirectory.getAbsolutePath());
    }
}
