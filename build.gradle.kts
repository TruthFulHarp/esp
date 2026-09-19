plugins {
    id("fabric-loom") version "1.16-SNAPSHOT"
    `maven-publish`
}

group = project.property("maven_group")!!
version = project.property("mod_version")!!

base {
    archivesName.set(project.property("archives_base_name") as String)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.fabricmc.net/") }
    maven { url = uri("https://maven.meteordevelopment.com/releases") }
    maven { url = uri("https://maven.meteordevelopment.com/snapshots") }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("meteordevelopment:meteor-client:${project.property("meteor_version")}")
}

tasks.processResources { 
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version")
        )
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
    options.encoding = "UTF-8"
}

tasks.jar {
    from("LICENSE") {
        rename { "LICENSE_CaveAirESP" }
    }
}
