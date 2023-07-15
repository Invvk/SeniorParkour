plugins {
    id("java")
}

group = "io.github.invvk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":NMS"))
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
}
