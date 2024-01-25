// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    extra.apply {
        set("libs", libs)
    }
    dependencies {
        classpath(libs.testingJunitJupiterPlugin)
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}