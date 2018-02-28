# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\IDE\Android\android-sdk-windows-20/tools/proguard/proguard-android.txt
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


#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class cn.limc.androidcharts.entity.** { *;}
-keep class com.example.app.bean.** { *;}
-keep class com.example.app.ui.view.activity.** { *;}
-keepclassmembers class com.example.app.base.Constants {
    public static <fields>;
}

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

-dontwarn io.netty.**
-keep class io.netty.**{ *;}

-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.**{ *;}

-keep class retrofit2.**{ *;}

-keep class io.reactivex.**{ *;}

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

-keep class com.raizlabs.android.dbflow.**{ *;}

-keep class de.greenrobot.event.**{ *;}

-keep class com.raizlabs.android.dbflow.**{ *;}
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

-keep class zhy.com.highlight.**{ *;}
-keep class com.andjdk.hvscrollviewlibrary.**{ *;}
-keep class com.linlong.ssa.marquee.**{ *;}
-keep class com.kyleduo.switchbutton.**{ *;}
-keep class com.carlos.voiceline.mylibrary.**{ *;}

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

#-keepclasseswithmembers class com.demo.login.bean.ui.MainActivity$JSInterface {
#      <methods>;
#}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

-dontwarn sun.**
-keep class sun.reflect.**{ *;}
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.**{ *;}
-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.**{ *;}

#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, LocalVariable*Table, *Annotation*, Synthetic, EnclosingMethod
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-dontwarn android.support.**#不提示兼容库的错误警告

-keepclasseswithmembernames class * {#不混淆jni方法
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{#不混淆Activity中参数类型为View的所有方法
    public void *(android.view.View);
}
-keepclassmembers enum * {#不混淆Enum类型的指定方法
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{#所有View的子类及其子类的get、set方法都不进行混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {#不混淆Parcelable和它的子类，还有Creator成员变量
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class **.R$* {#不混淆R类里及其所有内部static类中的所有static变量字段
    public static <fields>;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, jav.lang.String);
#}
