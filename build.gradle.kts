plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("kapt") version "1.9.10"
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.deltapvp.net/") {
        content {
            includeGroup("org.mineorbit.libby")
        }
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    compileOnly(libs.configurate)
    implementation(libs.libby)
    implementation(libs.bstats)

    compileOnly(libs.miniplaceholders.api)
    compileOnly(libs.miniplaceholders.kotlin)
}

sourceSets {
    main {
        blossom {
            kotlinSources {
                property("version", project.version.toString())
                property("configurate", libs.versions.configurate.get())
                property("geantyref", libs.versions.geantyref.get())
            }
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    clean {
        delete("run")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        arrayOf(
            "org.spongepowered",
            "net.byteflux",
            "io.leangen.geantyref",
            "org.bstats"
        ).forEach {
            relocate(it, "${rootProject.group}.libs.$it")
        }
    }
    compileKotlin {
        kotlinOptions {
            languageVersion = "1.9"
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

