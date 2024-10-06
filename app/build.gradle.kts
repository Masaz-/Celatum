plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "fi.masaz.celatum"
    compileSdk = 34

    defaultConfig {
        applicationId = "fi.masaz.celatum"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    }
}

dependencies {
    val coreVersion = "1.13.1"
    val roomVersion = "2.6.1"
    val biometricVersion = "1.1.0"
    val activityVersion = "1.9.2"
    val appCompatVersion = "1.7.0"
    val materialVersion = "1.12.0"
    val constraintLayoutVersion = "2.1.4"
    val flexboxVersion = "3.0.0"
    val recycleViewVersion = "1.3.2"
    val navigationVersion = "2.8.2"

    implementation("androidx.core:core-ktx:$coreVersion")
    implementation("androidx.biometric:biometric:$biometricVersion")
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("com.google.android.flexbox:flexbox:$flexboxVersion")
    implementation("androidx.recyclerview:recyclerview:$recycleViewVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}