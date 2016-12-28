# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Wen_en/Downloads/android-sdk-macosx/tools/proguard/proguard-android.txt
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
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/,!class/merging/
-printmapping mapping.txt

-keepattributes EnclosingMetho

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

 ##butterknife
 -keep class butterknife.** { *;}
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *;}

 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }

 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }

 ##RichText
 -dontwarn com.zzhoujay.richtext.**
 -keep class com.zzhoujay.richtext.**{*;}


 -dontwarn android.app.Notification.**

 -keep class android.app.Notification.**{*;}

 -dontwarn android.support.v4.**

 -keep class android.support.v4.**{*;}

 -dontwarn android.support.v7.**
 -keep class android.support.v7.**{*;}

 -dontwarn android.support.v13.**
 -keep class android.support.v13.**{*;}

 -keep  class javax.crypto.**{*;}
 -keep  class javax.microedition.**{*;}
 -keep  class javax.net.**{*;}
 -keep  class javax.security.**{*;}
 -keep  class android.webkit.**{*;}

 -dontwarn com.nostra13.universalimageloader.**
 -keep class com.nostra13.universalimageloader.**{*;}

 -keep class * implements java.io.Serializable

 -keep class com.wenen.literead.model.**{*;}

 -keepclassmembers class * implements java.io.Serializable{
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }
 -keep class **.R$* {
  *;
 }
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.app.Fragment
 -keep public class * extends android.app.Activity
 -keep public class * extends android.support.v4.app.FragmentActivity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
 }
 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keepclasseswithmembers class * {
     public <init>();
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepclassmembers class **.R$* {
     public static <fields>;
 }

 -keepclassmembers class * extends android.app.Activity {
     public void *(android.view.View);
 }
 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 # For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
 -keepclassmembers enum  * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 # Keep names - Native method names. Keep all native class/method names.
 -keepclasseswithmembers,allowshrinking class * {
     native <methods>;
 }

 # Keep names - _class method names. Keep all .class method names. This may be
 # useful for libraries that will be obfuscated again with different obfuscators.
 -keepclassmembers,allowshrinking class * {
     java.lang.Class class$(java.lang.String);
     java.lang.Class class$(java.lang.String,boolean);
 }

 -ignorewarnings
 -keep class android.support.design.widget.** { *;}
 -keep interface android.support.design.widget.** { *;}
 -dontwarn android.support.design.**
