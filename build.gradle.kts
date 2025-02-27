// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    /*dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }*/
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("io.realm.kotlin") version "1.16.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}