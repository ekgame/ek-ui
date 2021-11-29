plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":koui-core"))
    implementation("org.processing:core:3.3.7")
    implementation("dev.romainguy:kotlin-math:1.1.0")
    implementation("com.github.ajalt.colormath:colormath:3.1.1")
}
