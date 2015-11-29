# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/zengjinlong/android/android-sdk-linux/tools/proguard/proguard-android.txt
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

# must keep these for using LiteOrm lib
-keepattributes *Annotation*,Signature,Exceptions
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}

-dontwarn com.squareup.okhttp.**
-keep public class org.apache.http.** { *;}

# keep for butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Keep the support library
-dontwarn android.support.v4.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
