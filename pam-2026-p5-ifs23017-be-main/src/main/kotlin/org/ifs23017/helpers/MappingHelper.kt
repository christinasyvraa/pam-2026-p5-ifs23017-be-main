package org.ifs23017.helpers

import kotlinx.coroutines.Dispatchers
import org.ifs23017.dao.TodoDAO
import org.ifs23017.dao.RefreshTokenDAO
import org.ifs23017.dao.UserDAO
import org.ifs23017.entities.Todo
import org.ifs23017.entities.RefreshToken
import org.ifs23017.entities.User
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun userDAOToModel(dao: UserDAO) = User(
    dao.id.value.toString(),
    dao.name,
    dao.username,
    dao.password,
    dao.photo,
    dao.createdAt,
    dao.updatedAt
)

fun refreshTokenDAOToModel(dao: RefreshTokenDAO) = RefreshToken(
    dao.id.value.toString(),
    dao.userId.toString(),
    dao.refreshToken,
    dao.authToken,
    dao.createdAt,
)

fun todoDAOToModel(dao: TodoDAO) = Todo(
    id = dao.id.value.toString(),
    userId = dao.userId.toString(),
    title = dao.title,
    description = dao.description,
    urgency = dao.urgency,
    isDone =  dao.isDone,
    cover = dao.cover,
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)
