# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
-keep public class com.bytedance.sdk.openadsdk.a.d
# 方法名等混淆指定配置
-obfuscationdictionary fuckcoolapk-dic.txt
# 类名混淆指定配置
-classobfuscationdictionary fuckcoolapk-dic.txt
# 包名混淆指定配置
-packageobfuscationdictionary fuckcoolapk-dic.txt
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
 public static void check*(...);
 public static void throw*(...);
}

-assumenosideeffects class com.fuckcoolapk.utils.LogUtil {
 public static void _d*(...);
}