plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("kapt") version "1.8.20"
    alias(libs.plugins.blossom)
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") {
        content {
            includeGroup("com.velocitypowered")
        }
    }
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.AlessioDP.libby")
        }
    }
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib", "1.8.20"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    compileOnly(libs.configurate)
    implementation(libs.libby)
    implementation(libs.bstats)
}

val url: String by project
val id: String by project

blossom {
    replaceTokenIn("src/main/kotlin/io/github/_4drian3d/vlobby/utils/Constants.kt")
    replaceToken("{name}", rootProject.name)
    replaceToken("{id}", id)
    replaceToken("{version}", project.version)
    replaceToken("{description}", project.description)
    replaceToken("{url}", url)
    replaceToken("{configurate}", libs.versions.configurate.get())
    replaceToken("{geantyref}", libs.versions.geantyref.get())
}

tasks{
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
            languageVersion = "1.8"
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
