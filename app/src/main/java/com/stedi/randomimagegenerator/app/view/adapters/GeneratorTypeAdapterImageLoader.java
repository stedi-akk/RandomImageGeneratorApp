package com.stedi.randomimagegenerator.app.view.adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.callbacks.GenerateCallback;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Completable;
import rx.Scheduler;

@Singleton
public class GeneratorTypeAdapterImageLoader {
    private final LruCache<GeneratorType, CacheItem> cache = new LruCache<>(GeneratorType.values().length);

    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final Logger logger;

    private static class CacheItem {
        private final GeneratorParams params;
        private final Bitmap bitmap;

        CacheItem(GeneratorParams params, Bitmap bitmap) {
            this.params = params;
            this.bitmap = bitmap;
        }
    }

    interface Callback {
        void onLoaded(@NonNull GeneratorParams params, @NonNull Bitmap bitmap);
    }

    @Inject
    GeneratorTypeAdapterImageLoader(@Named("RigScheduler") Scheduler subscribeOn,
                                    @Named("UiScheduler") Scheduler observeOn,
                                    Logger logger) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.logger = logger;
    }

    void load(@NonNull GeneratorType type, @NonNull Callback callback) {
        CacheItem cacheItem = cache.get(type);
        if (cacheItem != null) {
            callback.onLoaded(cacheItem.params, cacheItem.bitmap);
            return;
        }

        Completable.fromAction(() -> {
            GeneratorParams params;
            if (type.isEffect()) {
                params = GeneratorParams.createDefaultEffectParams(type,
                        GeneratorParams.createDefaultParams(GeneratorType.COLORED_CIRCLES));
            } else {
                params = GeneratorParams.createDefaultParams(type);
            }
            new Rig.Builder()
                    .setGenerator(params.createGenerator())
                    .setCount(1)
                    .setFixedSize(200, 200)
                    .setCallback(new GenerateCallback() {
                        @Override
                        public void onGenerated(ImageParams imageParams, Bitmap bitmap) {
                            cache.put(type, new CacheItem(params, bitmap));
                            Completable.fromAction(() -> {
                                callback.onLoaded(params, bitmap);
                            }).subscribeOn(observeOn).subscribe();
                        }

                        @Override
                        public void onFailedToGenerate(ImageParams imageParams, Exception e) {
                            logger.log(this, "onFailedToGenerate", e);
                        }
                    }).build().generate();
        }).subscribeOn(subscribeOn).subscribe();
    }
}
