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
        versionCode = 2
        versionName = "0.$versionCode"
    }

    buildTypes {
        getByName("debug") {
//            applicationIdSuffix = ""
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
        /*
        getByName("release") {
            applicationIdSuffix = ""
            versionNameSuffix = ""
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.create(name) {
                storeFile = File(rootDir, "key.pkcs12")
                val properties = Properties().also {
                    File(rootDir, "properties").inputStream().use(it::load)
                }
                storePassword = properties["password"] as String
                keyPassword = storePassword
                keyAlias = "debug" // todo
            }
        }
        */
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
    implementation("com.huawei.hms:hwid:6.4.0.300")
//    implementation("com.huawei.hms:location:3.0.2.300") // 2019/10/31
//    implementation("com.huawei.hms:location:4.0.4.300") // 2020-05-29
//    implementation("com.huawei.hms:location:5.1.0.305") // 2021-09-29
//    implementation("com.huawei.hms:location:6.0.0.302") // 2021-07-15
//    implementation("com.huawei.hms:location:6.2.0.300") // 2021-11-01
//    implementation("com.huawei.hms:location:6.3.0.300") // 2022-01-15
    implementation("com.huawei.hms:location:6.4.0.300") // 2022-04-01
//    implementation("com.huawei.hms:push:6.1.0.300") // 2021-09-13
//    implementation("com.huawei.hms:push:6.3.0.302")
    implementation("com.huawei.hms:push:6.5.0.300") // 2022-05-10
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
}
