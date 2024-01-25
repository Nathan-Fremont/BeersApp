plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.nathanfremont.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"https://api.punkapi.com/v2\"")
            buildConfigField("boolean", "IS_LOGGABLE", "false")
        }

        debug {
            isMinifyEnabled = false
            buildConfigField("String", "API_URL", "\"https://api.punkapi.com/v2\"")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.hilt)
    api(libs.timber)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)

    releaseImplementation(libs.bundles.retrofitAndOkHttpProd)
    debugImplementation(libs.bundles.retrofitAndOkHttpPreprod)

    testImplementation(libs.junit)
    testImplementation(project(":coreTest"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}