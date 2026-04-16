package org.ifs23017.helpers

import io.ktor.server.application.*
import org.ifs23017.tables.RefreshTokenTable
import org.ifs23017.tables.TodoTable
import org.ifs23017.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    fun readConfig(path: String): String? =
        environment.config.propertyOrNull(path)?.getString()?.trim()?.takeIf { it.isNotEmpty() }
            ?.takeUnless { it.startsWith("\${") && it.endsWith("}") } // placeholder env var yang belum terisi

    val dbHost = readConfig("ktor.database.host")
    val dbPort = readConfig("ktor.database.port")
    val dbName = readConfig("ktor.database.name")
    val dbUser = readConfig("ktor.database.user")
    val dbPassword = readConfig("ktor.database.password")

    val missing = buildList {
        if (dbHost == null) add("DB_HOST")
        if (dbPort == null) add("DB_PORT")
        if (dbName == null) add("DB_NAME")
        if (dbUser == null) add("DB_USER")
        if (dbPassword == null) add("DB_PASSWORD")
    }

    if (missing.isNotEmpty()) {
        error(
            "Konfigurasi database belum lengkap: ${missing.joinToString(", ")}. " +
                "Isi env var tersebut (mis. lewat file .env) sebelum menjalankan server."
        )
    }

    Database.connect(
        url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        user = dbUser!!,
        password = dbPassword!!
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            UserTable,
            RefreshTokenTable,
            TodoTable
        )
    }
}
