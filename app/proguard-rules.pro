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

-ignorewarnings
-optimizationpasses 5
-printseeds seeds.txt
-printusage unused.txt
-printmapping build/outputs/mapping/release/mapping.txt

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable, LocalVariableTable,LocalVariableTypeTable
-repackageclasses ''

-keep class com.techdev.goalbuzz.model.* { *; }
-keep class com.techdev.goalbuzz.model.fixture.* { *; }
-keep class com.techdev.goalbuzz.model.league.* { *; }
-keep class com.techdev.goalbuzz.model.live.* { *; }
-keep class com.techdev.goalbuzz.model.point.* { *; }
-keep class com.techdev.goalbuzz.model.team.* { *; }
-keep class com.techdev.goalbuzz.model.top.* { *; }
-keep class com.techdev.goalbuzz.model.market.* { *; }

-keep class com.techdev.goalbuzz.featureExplores.domain.models.* { *; }

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.PlatformJava8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepattributes SourceFile,LineNumberTable

-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault