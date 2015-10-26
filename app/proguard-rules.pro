# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/fei/Dev/sdk/tools/proguard/proguard-android.txt
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

-dontwarn com.maxleap.**
-keep class com.maxleap.** { *; }

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class com.maxleapmobile.gitmaster.model.** { *; }
-dontwarn com.maxleapmobile.gitmaster.model.**




