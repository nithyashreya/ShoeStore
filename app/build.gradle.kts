plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}


android {
    namespace = "com.example.shoestore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shoestore"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.navigation:navigation-fragment:2.5.0")
    implementation ("androidx.navigation:navigation-ui:2.5.0")
    implementation ("com.google.android.material:material:1.6.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

}