package org.ifs23017

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.ifs23017.helpers.JWTConstants
import org.ifs23017.helpers.configureDatabases
import org.ifs23017.module.appModule
import org.koin.ktor.plugin.Koin
import java.net.InetSocketAddress
import java.net.ServerSocket

private fun canBind(host: String?, port: Int): Boolean {
    return try {
        ServerSocket().use { socket ->
            socket.reuseAddress = true
            if (!host.isNullOrBlank()) {
                socket.bind(InetSocketAddress(host, port))
            } else {
                socket.bind(InetSocketAddress(port))
            }
        }
        true
    } catch (_: Exception) {
        false
    }
}

private fun pickAvailablePort(host: String?, preferredPort: Int, maxTries: Int = 50): Int {
    if (preferredPort <= 0) return preferredPort
    if (canBind(host, preferredPort)) return preferredPort
    for (p in (preferredPort + 1)..(preferredPort + maxTries)) {
        if (canBind(host, p)) return p
    }
    return preferredPort
}

fun main(args: Array<String>) {
    val dotenv = dotenv {
        directory = "."
        // Jangan crash kalau .env tidak ada (mis. saat deploy / run dari working dir berbeda).
        // Env var / system property yang diset dari luar tetap bisa dipakai.
        ignoreIfMissing = true
    }

    dotenv.entries().forEach {
        // Jangan menimpa system property yang sudah diset (mis. dari environment CI/CD).
        if (System.getProperty(it.key).isNullOrBlank()) {
            System.setProperty(it.key, it.value)
        }
    }

    // Hindari crash `Address already in use` saat development.
    // Kalau port yang diminta sudah terpakai, pilih port berikutnya yang kosong.
    if (System.getProperty("io.ktor.development")?.toBoolean() == true) {
        val host = System.getProperty("APP_HOST")?.trim()?.ifEmpty { null }
        val preferredPort = System.getProperty("APP_PORT")?.toIntOrNull()
        if (preferredPort != null) {
            val chosenPort = pickAvailablePort(host, preferredPort)
            if (chosenPort != preferredPort) {
                System.setProperty("APP_PORT", chosenPort.toString())
                println("APP_PORT $preferredPort sudah dipakai, pakai port $chosenPort")
            }
        }
    }

    EngineMain.main(args)
}

fun Application.module() {
    val jwtSecret = environment.config.property("ktor.jwt.secret").getString().trim()
        .ifEmpty { "dev-secret-change-me" }

    install(Authentication) {
        jwt(JWTConstants.NAME) {
            realm = JWTConstants.REALM

            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(JWTConstants.ISSUER)
                    .withAudience(JWTConstants.AUDIENCE)
                    .build()
            )

            validate { credential ->
                val userId = credential.payload
                    .getClaim("userId")
                    .asString()

                if (!userId.isNullOrBlank())
                    JWTPrincipal(credential.payload)
                else null
            }

            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf(
                        "status" to "error",
                        "message" to "Token tidak valid"
                    )
                )
            }
        }
    }

    install(CORS) {
        anyHost()
    }

    install(ContentNegotiation) {
        json(
            Json {
                explicitNulls = false
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(Koin) {
        modules(appModule(jwtSecret))
    }

    configureDatabases()
    configureRouting()
}
