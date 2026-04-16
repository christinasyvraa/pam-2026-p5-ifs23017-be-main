package org.ifs23017.dao

import org.ifs23017.tables.UserTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import java.util.UUID

class UserDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserDAO>(UserTable)

    var name by UserTable.name
    var username by UserTable.username
    var password by UserTable.password
    var photo by UserTable.photo
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
}