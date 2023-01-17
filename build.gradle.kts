plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("kapt") version "1.7.22"
    id("net.kyori.blossom") version "1.3.1"
    id("xyz.jpenilla.run-velocity") version "2.0.0"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib", "1.7.21"))
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
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
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}


kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

java {
    disableAutoTargetJvm()
}
