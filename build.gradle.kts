plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

group = "io.github.invvk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    implementation(project(":NMS"))
    implementation(project(":V1_19", "reobf"))
    implementation(project(":V1_18", "reobf"))
    implementation(project(":V1_17", "reobf"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("ch.jalu:configme:1.3.1")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

