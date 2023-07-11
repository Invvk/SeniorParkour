plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

group = "io.github.invvk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    implementation(project(":NMS"))
}