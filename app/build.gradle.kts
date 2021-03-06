repositories {
    mavenCentral()
    google()
}

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = compileSdk
        applicationId = "test.android.hms"
        versionCode = 1
        versionName = "0.$versionCode"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    applicationVariants.all {
        outputs.forEach { output ->
            check(output is com.android.build.gradle.internal.api.ApkVariantOutputImpl)
            output.versionCodeOverride = versionCode
            output.outputFileName = "$applicationId-$versionName-$versionCode.apk"
        }
    }
}

dependencies {
    // todo
}
