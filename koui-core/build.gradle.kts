plugins {
    kotlin("multiplatform") version "1.6.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {}
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("dev.romainguy:kotlin-math:1.1.0")
                implementation("com.github.ajalt.colormath:colormath:3.1.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}