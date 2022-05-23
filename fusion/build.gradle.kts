import org.jetbrains.kotlin.gradle.plugin.statistics.ReportStatisticsToElasticSearch.url

plugins {
    id ("com.android.library")
    id ("maven-publish")
    kotlin("android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 24
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

afterEvaluate {
    publishing {
        repositories {
            maven {
                credentials {
                    username = "admin"
                    password = "auto123"
                }
                uri("http://localhost:8081/repository/mail/")
                isAllowInsecureProtocol = true
            }
        }
        publications {
            create<MavenPublication>("binary") {
                groupId = "me.proton"
                artifactId = "fusion"
                version = "1.0.0"
                from(components["release"])
                artifact ("build/outputs/aar/fusion-release.aar")
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.ui:ui-test:1.1.1")
    implementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("androidx.test.espresso:espresso-contrib:3.4.0")
    implementation("androidx.test.espresso:espresso-intents:3.4.0")
    implementation("androidx.test.espresso:espresso-web:3.4.0")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.test:core-ktx:1.4.0")
}
