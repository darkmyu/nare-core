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
    set("pluginClass", "Core")
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
            val plugins = File(rootDir, "server/plugins/")
            val update = File(plugins, "update")

            copy {
                from(archiveFile)
                into(if (File(plugins, archiveFileName.get()).exists()) File(plugins, "update") else plugins)
            }

            File(update, "RELOAD").delete()
        }
    }
}

idea {
    module {
        excludeDirs.add(file("server"))
    }
}
