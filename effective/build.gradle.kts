import net.fabricmc.loom.api.LoomGradleExtensionAPI

val effectiveModId: String by project
val effectiveModVersion: String by rootProject
val effectiveMavenGroup: String by rootProject
val minecraftVersion: String by rootProject

plugins {
    kotlin("jvm")
    id("io.github.pacifistmc.forgix")
    id("architectury-plugin")
    id("dev.architectury.loom-no-remap") apply false
    id("com.gradleup.shadow") apply false
}

architectury {
    minecraft = minecraftVersion
}

forgix {
    group = effectiveMavenGroup
    mergedJarName = "$effectiveModId-$effectiveModVersion.jar"
    outputDir = "build"
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom-no-remap")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom").apply {
        silentMojangMappingsLicense()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    kotlin {
        jvmToolchain(25)
    }

    base {
        archivesName = "$effectiveModId-${project.name}"
    }

    repositories {
        mavenCentral()
        maven("https://maven.parchmentmc.org")
        maven("https://maven.terraformersmc.com/")
        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        maven("https://cursemaven.com")
        maven("https://maven.uuid.gg/releases") // Velvet API
//        maven("https://api.modrinth.com/maven")
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        @Suppress("UnstableApiUsage") "mappings"(loom.officialMojangMappings())
    }

}
