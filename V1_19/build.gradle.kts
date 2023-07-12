plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

group = "io.github.invvk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation(project(":NMS"))
}

configurations.reobf {
    extendsFrom(configurations.apiElements.get(), configurations.runtimeElements.get())
}
