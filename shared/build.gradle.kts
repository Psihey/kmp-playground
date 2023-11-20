import Build_gradle.Versions.koin
import com.codingfeline.buildkonfig.compiler.FieldSpec


plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("com.codingfeline.buildkonfig") version "+"
}

// Dependencies.kt

object Versions {
    const val koin = "1.1.0"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:latest_version")
    }
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val voyagerVersion = "1.0.0-rc10"
                implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                // Koin integration
                implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
                implementation("io.insert-koin:koin-compose:1.1.0")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("co.touchlab:kermit:2.0.2")
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation("media.kamel:kamel-image:0.6.0")
                implementation("io.ktor:ktor-client-core:2.3.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.6.1")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.9.0")
                implementation("io.ktor:ktor-client-android:2.3.1")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.1")
            }
        }
    }

}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

}

buildkonfig {
    packageName = "com.myapplication"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "test", "testvalue")
        buildConfigField(FieldSpec.Type.STRING, "target", "common")
        buildConfigField(FieldSpec.Type.STRING, "testKey1", null, nullable = true)
        buildConfigField(FieldSpec.Type.STRING, "testKey2", "testValue2", nullable = false)
        buildConfigField(FieldSpec.Type.STRING, "testKey3", "testValue3", nullable = false, const = true)
    }

    targetConfigs {
        create("android") {
            buildConfigField(FieldSpec.Type.STRING, "target", "android")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "target", "ios")
        }
        // flavor is passed as a first argument of targetConfigs
    }
    targetConfigs("dev") {
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "target", "devValueIos")
        }
        create("android"){
            buildConfigField(FieldSpec.Type.STRING, "target", "devValueAndroid")
        }
    }
}


