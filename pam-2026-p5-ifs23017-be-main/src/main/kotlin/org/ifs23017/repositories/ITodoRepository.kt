package org.ifs23017.repositories

import org.ifs23017.entities.Todo

interface  ITodoRepository {
    suspend fun getAll(userId: String, search: String, perPage: Int, offset: Long): List<Todo>
    suspend fun countAll(userId: String, search: String): Long
    suspend fun getById(todoId: String): Todo?
    suspend fun create(todo: Todo): String
    suspend fun update(userId: String, todoId: String, newTodo: Todo): Boolean
    suspend fun delete(userId: String, todoId: String) : Boolean
}
