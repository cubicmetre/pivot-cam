plugins {
	id("dev.kikugie.loom-back-compat")
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = property("mod.id") as String

val requiredJava: JavaVersion = when {
	sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
	else -> JavaVersion.VERSION_21
}

repositories {
}

dependencies {
	minecraft("com.mojang:minecraft:${sc.current.version}")
	loomx.applyMojangMappings()

	modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${sc.properties.get<String>("deps.fabric_api")}")
}

loom {
	runConfigs.all {
		preferGradleTask = true
		generateRunConfig = true
		runDirectory = rootProject.file("run")
	}
}

java {
	withSourcesJar()
	targetCompatibility = requiredJava
	sourceCompatibility = requiredJava

	toolchain {
		vendor = JvmVendorSpec.ADOPTIUM
		languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
	}
}

tasks {
	processResources {
		fun MutableMap<String, String>.register(key: String, value: String) {
			inputs.property(key, value)
			set(key, value)
		}

		val props = buildMap {
			register("id", sc.properties.get<String>("mod.id"))
			register("name", sc.properties.get<String>("mod.name"))
			register("version", "${sc.properties.get<String>("mod.version")}+${sc.current.version}")
			register("minecraft", sc.properties.get<String>("mod.mc_compat"))
			register("loader", Regex("\\d+\\.\\d+").find(project.property("deps.fabric_loader") as String)!!.value)
			register("java", "${requiredJava.majorVersion}")
		}

		filesMatching("fabric.mod.json") { expand(props) }

		val mixinJava = "JAVA_${requiredJava.majorVersion}"
		filesMatching("*.mixins.json") { expand("java" to mixinJava) }
	}

	withType<Jar> {
		val name = project.property("mod.id")
		inputs.property("mod_id", name)
		from(rootProject.file("LICENSE")) {
			rename { "${it}_${name}" }
		}
	}

	register<Copy>("buildAndCollect") {
		group = "build"
		description = "Builds mod jars and copies results to build/libs/{mod version}/"

		dependsOn(loomx.modJar)
		inputs.property("version", project.property("mod.version"))
		from(loomx.modJar.flatMap { it.archiveFile })
		into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
	}
}
