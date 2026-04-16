package org.ifs23017.repositories

import org.ifs23017.entities.User

interface  IUserRepository {
    suspend fun getById(userId: String): User?
    suspend fun getByUsername(username: String): User?
    suspend fun create(user: User) : String
    suspend fun update(id: String, newUser: User): Boolean
    suspend fun delete(id: String): Boolean
}