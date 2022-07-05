buildscript {
    repositories {
        mavenCentral()
        google()
        maven("https://developer.huawei.com/repo/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.huawei.agconnect:agcp:1.7.0.300")
    }
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
