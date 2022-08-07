buildscript {
    repositories {
        mavenCentral()
        google()
        maven("https://developer.huawei.com/repo/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
//        classpath("com.huawei.agconnect:agcp:1.1.0.300") // 2019-11
//        classpath("com.huawei.agconnect:agcp:1.2.1.301") // 2020-03-05
//        classpath("com.huawei.agconnect:agcp:1.3.2.301") // 2020-06-16
//        classpath("com.huawei.agconnect:agcp:1.4.2.301") // 2020-11-20
//        classpath("com.huawei.agconnect:agcp:1.5.2.300") // 2021-05-17
        classpath("com.huawei.agconnect:agcp:1.7.0.300") // 2022-06-16
    }
}

task<Delete>("clean") {
    delete = setOf(buildDir)
}
