// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        android.set(true)
        verbose.set(true)
        ignoreFailures.set(true)
        filter {
            exclude {
                it.file.path.contains("generated/") ||
                it.file.path.contains("test/") ||
                it.file.path.contains("androidTest/")
            }
        }
    }
}
