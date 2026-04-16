package org.ifs23017.data

import kotlinx.serialization.Serializable
import org.ifs23017.entities.Todo

@Serializable
data class TodoListMeta(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val total: Long
)

@Serializable
data class TodoListResponse(
    val todos: List<Todo>,
    val meta: TodoListMeta
)
