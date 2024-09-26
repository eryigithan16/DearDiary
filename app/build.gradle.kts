plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("io.realm.kotlin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "1.9.23"
}

android {
    namespace = "com.example.deardiary"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.deardiary"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose Navigation
    implementation (libs.androidx.navigation.compose)

    implementation (libs.kotlinx.serialization.json)

    // Firebase
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.storage.ktx)

    // Room components
    implementation (libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    // Runtime Compose
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Splash API
    implementation ("androidx.core:core-splashscreen:1.0.1")

    // Mongo DB Realm
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.library.sync)

    // Dagger Hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    // Coil
    implementation (libs.coil.compose)

    // Date-Time Picker
    implementation (libs.date.time.picker)

    // CALENDAR
    implementation (libs.calendar)

    // CLOCK
    implementation (libs.clock)

    // Message Bar Compose
    implementation (libs.messagebarcompose)

    // One-Tap Compose
    implementation (libs.onetapcompose)

    // Desugar JDK
    coreLibraryDesugaring (libs.desugar.jdk.libs)

    //One Tab Google
    implementation("com.github.stevdza-san:OneTapCompose:1.0.7")

    //Message Bar
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:mongo"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:write"))
}
kapt {
    correctErrorTypes = true
}