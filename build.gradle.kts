plugins {
    kotlin("jvm") version Configuration.Versions.kotlin apply false
    kotlin("plugin.serialization") version Configuration.Versions.kotlin apply false
    // see: https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
}