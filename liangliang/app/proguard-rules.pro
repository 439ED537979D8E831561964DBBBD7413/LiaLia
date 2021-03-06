# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\toolbs\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.alibaba.**
-keep class com.alibaba.**
-keepclassmembers class com.alibaba.** {
   *;
}
-keep class com.taobao.**
-keepclassmembers class com.taobao.** {
    *;
}

-dontwarn com.google.common.**
-dontwarn com.fasterxml.jackson.**
-dontwarn com.amap.api.**
-dontwarn net.jcip.annotations.**

-keepattributes Annotation,EnclosingMethod,Signature
-keep class com.fasterxml.jackson.**
-keepclassmembers class com.fasterxml.jackson.** {
   *;
}

-keep class com.duanqu.**
-keepclassmembers class com.duanqu.** {
    *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class [cn.chono.yopper].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep class * extends java.lang.annotation.Annotation { *; }

-dontwarn android.net.compatibility.** -dontwarn   android.net.http.**
-dontwarn   com.android.internal.http.multipart.** -dontwarn   org.apache.commons.**
-dontwarn   org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

-dontwarn android.support.multidex.**
-keep class android.support.multidex.**
-keepclassmembers class android.support.multidex.** {
   *;
}

-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**
