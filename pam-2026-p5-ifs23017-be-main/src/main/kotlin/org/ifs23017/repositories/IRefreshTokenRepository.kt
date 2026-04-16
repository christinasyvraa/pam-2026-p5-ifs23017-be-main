package org.ifs23017.repositories

import org.ifs23017.entities.RefreshToken

interface  IRefreshTokenRepository {
    suspend fun getByToken(refreshToken: String, authToken: String): RefreshToken?
    suspend fun create(newRefreshToken: RefreshToken) : String
    suspend fun delete(authToken: String): Boolean
    suspend fun deleteByUserId(userId: String): Boolean
}