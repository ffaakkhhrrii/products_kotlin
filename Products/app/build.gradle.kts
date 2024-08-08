plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.gms.google.services)
    //id ("androidx.room")
    id("com.google.dagger.hilt.android")
    //id ("org.jetbrains.kotlin.jvm")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.fakhri.products"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.fakhri.products"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://dummyjson.com/\"")
//        kapt{
//            arguments{
//                arg("room.schemaLocation","$projectDir/schemas")
//            }
//        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
        buildConfig = true
    }

    testOptions{
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.paging:paging-runtime:3.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("com.google.code.gson:gson:2.8.8")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ViewModel + Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation(libs.firebase.messaging)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.runner)
    testImplementation("junit:junit:4.12")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    //implementation("androidx.paging:paging-runtime:$room_version")
    implementation ("androidx.room:room-paging:$room_version")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")
    testImplementation ("org.mockito:mockito-core:5.3.1")
    //androidTestImplementation ("org.mockito:mockito-core:5.3.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    // Coroutines
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    implementation ("io.mockk:mockk:1.12.4")
    testImplementation ("app.cash.turbine:turbine:0.8.0")
    testImplementation ("androidx.paging:paging-common:3.1.1")
}

kapt {
    correctErrorTypes = true
}

