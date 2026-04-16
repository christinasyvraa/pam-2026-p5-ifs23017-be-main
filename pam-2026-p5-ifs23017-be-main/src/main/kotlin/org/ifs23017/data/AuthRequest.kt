package org.ifs23017.data

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.ifs23017.entities.User

@Serializable
data class AuthRequest(
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var newPassword: String = "",
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "username" to username,
            "password" to password,
            "newPassword" to newPassword
        )
    }

    fun toEntity(): User {
        return User(
            name = name,
            username = username,
            password = password,
            updatedAt = Clock.System.now()
        )
    }

}