import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	`maven-publish`
}

group = "jp.funmake"
version = Configuration.Versions.current
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	api("org.springframework:spring-context:${Configuration.Versions.spring}")
	api("org.springframework:spring-webflux:${Configuration.Versions.spring}")
	api("com.squareup.retrofit2:retrofit:${Configuration.Versions.retrofit}")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
		jvmTarget = "1.8"
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = project.group as String
			artifactId = rootProject.name
			version = project.version as String
			from(components["java"])

			pom {
				inceptionYear.set("2020")
				name.set(rootProject.name)
				description.set("Retrofit annotation adapter for Spring framework.")
				licenses {
					license {
						name.set("Apache License, Version 2.0")
						url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
						distribution.set("repo")
					}
				}
			}
		}
	}
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/nosix/retrofit-spring-adapter")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
}