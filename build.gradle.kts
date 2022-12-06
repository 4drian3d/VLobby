import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.kyori.blossom") version "1.3.1"
    id("xyz.jpenilla.run-velocity") version "2.0.0"
    kotlin("jvm") version "1.7.21"
    //kotlin("kapt") version "1.7.21"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib", "1.7.21"))
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    //kapt("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

val url = "https://github.com/4drian3d/VLobby"
val id = "vlobby"

blossom {
    replaceTokenIn("src/main/kotlin/me/adrian3d/vlobby/utils/Constants.kt")
	replaceToken("{name}", rootProject.name)
    replaceToken("{id}", id)
	replaceToken("{version}", project.version)
	replaceToken("{description}", project.description)
    replaceToken("{url}", url)
}

tasks{
    runVelocity {
        velocityVersion("3.1.2-SNAPSHOT")
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
