# Proguard rules for ECOLIM app
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-keep class com.ecolim.app.data.local.entities.** { *; }
-keep class com.ecolim.app.data.remote.dto.** { *; }
