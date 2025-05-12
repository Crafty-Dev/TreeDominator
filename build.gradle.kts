plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("xyz.jpenilla.run-paper") version "2.3.1"

}

group = "de.crafty.treedominator"
version = "0.5.0+1.21.4"

var buildOutput = project.properties["output"].toString()

repositories {
    mavenCentral()
    mavenLocal() // This is needed for CraftBukkit and Spigot.
}

dependencies {
    // Pick only one of these and read the comment in the repositories block.
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

}


tasks {

    runServer {
        minecraftVersion("1.21.4")
    }

    processResources {
        filesMatching("plugin.yml"){
            expand("version" to project.version)
        }
    }

    reobfJar {
        outputJar.set(file("$buildOutput/TreeDominator-${version}.jar"))
    }

    assemble {
        dependsOn(reobfJar)
    }
}
