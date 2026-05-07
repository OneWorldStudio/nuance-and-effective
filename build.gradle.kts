plugins {
    kotlin("jvm") version "2.2.21" apply false
    id("io.github.pacifistmc.forgix") version "1.2.9" apply false
    id("architectury-plugin") version "3.5-SNAPSHOT" apply false
    id("dev.architectury.loom-no-remap") version "1.14-SNAPSHOT" apply false
    id("com.gradleup.shadow") version "8.3.6" apply false
}

val effectiveModVersion: String by project
val effectiveModDescription: String by project
val effectiveMavenGroup: String by project

allprojects {
    group = effectiveMavenGroup
    version = effectiveModVersion
    description = effectiveModDescription
}
