import org.gradle.configurationcache.extensions.capitalized

plugins {
    idea
    kotlin("jvm") version "1.8.10"
    id("io.papermc.paperweight.userdev") version "1.4.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}

extra.apply {
    set("pluginName", project.name.split("-").joinToString("") { it.capitalized() })
    set("packageName", project.name.replace("-", ""))
    set("kotlinVersion", "1.8.10")
    set("paperVersion", "1.19")
}

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(project.extra.properties)
        }
    }

    create<Jar>("paperJar") {
        from(sourceSets["main"].output)
        archiveBaseName.set(project.extra.properties["pluginName"].toString())
        archiveVersion.set("")

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".server/plugins/")
                into(plugins)
            }
        }
    }
}

idea {
    module {
        excludeDirs.add(file(".server"))
    }
}
