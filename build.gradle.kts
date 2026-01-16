plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT"
}

val modVersion: String by project

version = "$modVersion+${libs.versions.minecraft.get()}"
group = "xyz.bonfiremc"

repositories {
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchment)
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    processResources {
        val minecraftVersion: String = libs.versions.minecraft.get()

        inputs.property("version", version)
        inputs.property("minecraft_version", minecraftVersion)

        filesMatching("fabric.mod.json") {
            expand(
                "version" to version,
                "minecraft_version" to minecraftVersion
            )
        }
    }

    jar {
        from("LICENSE")
    }
}
