// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
       // mavenCentral()
    }
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
       // classpath("org.jetbrains.kotlinx:kover-gradle-plugin:0.8.3")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
    id ("androidx.room") version "2.6.1" apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
//    id ("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    //id("org.jetbrains.kotlinx.kover") version "0.8.3" apply false
}