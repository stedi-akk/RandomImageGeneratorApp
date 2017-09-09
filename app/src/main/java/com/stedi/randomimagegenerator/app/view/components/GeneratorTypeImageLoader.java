package com.stedi.randomimagegenerator.app.view.components;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

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
    private final ArrayMap<GeneratorType, CacheItem> cache = new ArrayMap<>(GeneratorType.values().length);
    private final ArrayMap<GeneratorType, List<Callback>> callbacks = new ArrayMap<>();

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

    public void load(@NonNull GeneratorType type, @NonNull Callback callback) {
        CacheItem cacheItem = cache.get(type);
        if (cacheItem != null) {
            callback.onLoaded(cacheItem.params, cacheItem.bitmap);
            return;
        }

        if (callbacks.containsKey(type)) {
            callbacks.get(type).add(callback);
        } else {
            List<Callback> list = new ArrayList<>();
            list.add(callback);
            callbacks.put(type, list);
        }

        if (cache.containsKey(type))
            return;
        cache.put(type, null);

        Completable.fromAction(() -> {
            GeneratorParams params;
            if (type.isEffect()) {
                params = GeneratorParams.createDefaultEffectParams(type,
                        GeneratorParams.createDefaultParams(GeneratorType.COLORED_CIRCLES));
            } else {
                params = GeneratorParams.createDefaultParams(type);
            }
            new Rig.Builder()
                    .setGenerator(params.getGenerator())
                    .setCount(1)
                    .setFixedSize(200, 200)
                    .setCallback(new GenerateCallback() {
                        @Override
                        public void onGenerated(ImageParams imageParams, Bitmap bitmap) {
                            uiHandler.post(() -> {
                                cache.put(type, new CacheItem(params, bitmap));
                                for (Callback callback : callbacks.get(type)) {
                                    callback.onLoaded(params, bitmap);
                                }
                                callbacks.remove(type);
                            });
                        }

                        @Override
                        public void onFailedToGenerate(ImageParams imageParams, Exception e) {
                            logger.log(GeneratorTypeImageLoader.this, "onFailedToGenerate", e);
                        }
                    }).build().generate();
        }).subscribeOn(subscribeOn).subscribe();
    }
}
