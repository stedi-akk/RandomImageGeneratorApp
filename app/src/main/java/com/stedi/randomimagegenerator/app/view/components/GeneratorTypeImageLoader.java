package com.stedi.randomimagegenerator.app.view.components;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.callbacks.GenerateCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Scheduler;

@Singleton
public class GeneratorTypeImageLoader {
    private final SparseArray<CacheItem> cache = new SparseArray<>();
    private final SparseArray<List<Callback>> callbacks = new SparseArray<>();

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final Scheduler subscribeOn;
    private final Logger logger;

    private static class CacheItem {
        private final GeneratorParams params;
        private final Bitmap bitmap;

        CacheItem(GeneratorParams params, Bitmap bitmap) {
            this.params = params;
            this.bitmap = bitmap;
        }
    }

    public interface Callback {
        void onLoaded(@NonNull GeneratorParams params, @NonNull Bitmap bitmap);
    }

    @Inject
    public GeneratorTypeImageLoader(@NonNull @RigScheduler Scheduler subscribeOn, @NonNull Logger logger) {
        this.subscribeOn = subscribeOn;
        this.logger = logger;
    }

    public void load(@NonNull GeneratorType mainType, @Nullable GeneratorType secondType, @NonNull Callback callback) {
        int key = createCacheKey(mainType, secondType);

        CacheItem cacheItem = cache.get(key);
        if (cacheItem != null) {
            callback.onLoaded(cacheItem.params, cacheItem.bitmap);
            return;
        }

        if (callbacks.indexOfKey(key) >= 0) {
            callbacks.get(key).add(callback);
        } else {
            List<Callback> list = new ArrayList<>();
            list.add(callback);
            callbacks.put(key, list);
        }

        if (cache.indexOfKey(key) >= 0)
            return;
        cache.put(key, null);

        Completable.fromAction(() -> {
            GeneratorParams params = createGeneratorParams(mainType, secondType);
            new Rig.Builder()
                    .setGenerator(params.getGenerator())
                    .setCount(1)
                    .setFixedSize(200, 200)
                    .setCallback(new GenerateCallback() {
                        @Override
                        public void onGenerated(ImageParams imageParams, Bitmap bitmap) {
                            uiHandler.post(() -> {
                                cache.put(key, new CacheItem(params, bitmap));
                                for (Callback callback : callbacks.get(key)) {
                                    callback.onLoaded(params, bitmap);
                                }
                                callbacks.remove(key);
                            });
                        }

                        @Override
                        public void onFailedToGenerate(ImageParams imageParams, Exception e) {
                            logger.log(GeneratorTypeImageLoader.this, "onFailedToGenerate", e);
                        }
                    }).build().generate();
        }).subscribeOn(subscribeOn).subscribe();
    }

    private int createCacheKey(GeneratorType mainType, GeneratorType secondType) {
        if (secondType == null) {
            if (mainType.isEffect())
                throw new IllegalStateException("incorrect behavior");
            return mainType.ordinal();
        } else {
            if (!mainType.isEffect() || secondType.isEffect())
                throw new IllegalStateException("incorrect behavior");
            return Integer.parseInt(mainType.ordinal() + "" + secondType.ordinal());
        }
    }

    private GeneratorParams createGeneratorParams(GeneratorType mainType, GeneratorType secondType) {
        if (secondType == null) {
            return GeneratorParams.createDefaultParams(mainType);
        } else {
            return GeneratorParams.createDefaultEffectParams(mainType, GeneratorParams.createDefaultParams(secondType));
        }
    }
}
