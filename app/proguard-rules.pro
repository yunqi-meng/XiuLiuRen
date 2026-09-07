# Keep Room generated classes
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep class androidx.room.* { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}