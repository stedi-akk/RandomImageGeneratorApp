# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\stedi\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Kotlin
-dontwarn kotlin.**

# OrmLite
-dontwarn com.j256.**
-keep class com.j256.**
-keepclassmembers class com.j256.** {*;}
-keep enum com.j256.**
-keepclassmembers enum com.j256.** {*;}
-keep interface com.j256.**
-keepclassmembers interface com.j256.** {*;}
-keepattributes *Annotation*
-keep class com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository
-keepclassmembers class com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository
-keep class com.stedi.randomimagegenerator.app.model.data.Preset
-keepclassmembers class com.stedi.randomimagegenerator.app.model.data.Preset
-keep class com.stedi.randomimagegenerator.app.model.data.generatorparams.**
-keepclassmembers class com.stedi.randomimagegenerator.app.model.data.generatorparams.** {*;}

# RxJava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# Otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# Picasso
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase