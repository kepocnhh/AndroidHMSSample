import java.util.Properties

repositories {
    mavenCentral()
    google()
    maven("https://developer.huawei.com/repo/")
}

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.huawei.agconnect")
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
            signingConfig = signingConfigs.getByName(name) {
                storeFile = File(rootDir, "key.pkcs12")
                val properties = Properties().also {
                    File(rootDir, "properties").inputStream().use(it::load)
                }
                storePassword = properties["password"] as String
                keyPassword = storePassword
                keyAlias = name
            }
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
    implementation("com.huawei.hms:location:6.4.0.300")
    implementation("com.huawei.hms:push:6.5.0.300")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
}
