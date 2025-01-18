buildscript {
    extra.apply {
        set("compose_compiler_version", "1.5.8")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // Set specific Kotlin version
}