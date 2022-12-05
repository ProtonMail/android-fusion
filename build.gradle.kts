plugins {
    `maven-publish`
    signing
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("http://localhost:8081/repository/mail/")
            isAllowInsecureProtocol = true
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.21.0")
//        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
    }
}
