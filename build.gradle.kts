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
    maven("https://s01.oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(project(":NMS"))
    implementation(project(":V1_19", "reobf"))
    implementation(project(":V1_18", "reobf"))
    implementation(project(":V1_17", "reobf"))
    implementation(project(":V1_16"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.github.rysefoxx.inventory:RyseInventory-Plugin:1.6.5")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

