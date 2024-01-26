plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nathanfremont.beersapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nathanfremont.beersapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "IS_LOGGABLE", "false")
        }

        debug {
            buildConfigField("boolean", "IS_LOGGABLE", "true")
        }
    }
    productFlavors {
        flavorDimensions += "env"

        create("preprod") {
            dimension = "env"
        }

        create("prod") {
            dimension = "env"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.timber)
    implementation(libs.coilCompose)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)
    implementation(project(":domain"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(project(":coreTest"))
    testImplementation(libs.bundles.commonTest)
    testImplementation(libs.testingJunitJupiterApi)
    kaptTest(libs.testingHilt)
    testRuntimeOnly(libs.testingJunitJupiterEngine)
}