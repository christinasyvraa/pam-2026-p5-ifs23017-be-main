package org.ifs23017.helpers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.ifs23017.data.AppException
import org.ifs23017.entities.User
import org.ifs23017.repositories.IUserRepository

object ServiceHelper {
     suspend fun getAuthUser(call: ApplicationCall, userRepository: IUserRepository): User {
        val principal = call.principal<JWTPrincipal>()
            ?: throw AppException(401, "Unauthorized")

        val userId = principal
            .payload
            .getClaim("userId")
            .asString()
            ?: throw AppException(401, "Token tidak valid")

        val user = userRepository.getById(userId)
            ?: throw AppException(401, "User tidak valid")

        return user
    }
}