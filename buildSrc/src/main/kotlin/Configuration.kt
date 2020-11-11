@file:Suppress("MemberVisibilityCanBePrivate")

object Configuration {
    object Versions {
        const val kotlin = "1.4.10"
        const val spring = "5.3.0-RC2"
        const val springBoot = "2.4.0-M4"
        const val retrofit = "2.9.0"

        const val patch = "0"

        val current = "${springBoot.toPart()}.${retrofit.toPart()}.$patch"

        private fun String.toPart(): String =
            this.split(".", "-").take(2).joinToString(separator = "") { "%02d".format(it.toInt()) }
    }
}