plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.io.sqldelight)
}

android {
  namespace = "com.companion.lol.sqldelight"
  compileSdk { version = release(36) }
}

sqldelight {
  databases {
    create("LolAppDb"){
      packageName.set("com.companion.lol.storage.sqldelight")

    }
  }
}

/*tasks.withType<app.cash.sqldelight.gradle.SqlDelightTask>().configureEach {
  doLast("workaround https://github.com/sqldelight/sqldelight/issues/1333") {
    outputDirectory.get().asFile.walk()
      .filter { it.isFile && it.extension == "kt" }
      .forEach { file ->
        println(file.name)
        file.writeText(
          file.readText()
            .replace("public interface ", "internal interface ")
            .replace("public class", "internal class")
            .replace("public data class ", "internal data class ")

            .replace("public companion object", "companion object")
            .replace("  public object ", "  object ")
            .replace("\npublic object ", "\ninternal object ")
            .replace("public data object ", "internal data object ")

            .replace("public operator fun ", "internal operator fun ")
            .replace("public suspend fun ", "internal suspend fun ")
            .replace("public fun ", "internal fun ")

            .replace("public val ", "val ")
        )
      }
  }
}*/

dependencies {
  api(libs.io.sqldelight)
  api(libs.io.sqldelight.adapters)
  api(libs.io.sqldelight.flow)
  api(libs.kotlinx.datetime)

  compileOnly(libs.androidx.compose.runtime.annotation)
}
