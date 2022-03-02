plugins {
    java
    id("net.kyori.blossom") version "1.3.0"
}

repositories {
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

group = "me.dreamerzero.vlobby"
version = "1.0.0"
description = "Lobby features"
val url = "https://github.com/4drian3d/VLobby"
val id = "vlobby"

java.sourceCompatibility = JavaVersion.VERSION_11

blossom{
	val constants = "src/main/java/me/dreamerzero/vlobby/utils/Constants.java"
	replaceToken("{name}", rootProject.name, constants)
    replaceToken("{id}", id, constants)
	replaceToken("{version}", version, constants)
	replaceToken("{description}", description, constants)
    replaceToken("{url}", url, constants)
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
