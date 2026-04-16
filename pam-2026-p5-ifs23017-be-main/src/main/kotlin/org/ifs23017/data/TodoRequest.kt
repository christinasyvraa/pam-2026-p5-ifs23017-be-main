package org.ifs23017.data

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.ifs23017.entities.Todo
import java.util.UUID

@Serializable
data class TodoRequest(
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    var urgency: String? = null,
    var cover: String? = null,
    var isDone: Boolean? = null,
){
    private val allowedUrgencies = setOf("HIGH", "MEDIUM", "LOW")

    private fun normalizedUrgencyValue(): String {
        return urgency!!.trim().uppercase()
    }

    fun isUrgencyValid(): Boolean {
        if (urgency.isNullOrBlank()) return false
        return allowedUrgencies.contains(normalizedUrgencyValue())
    }

    fun normalizedUrgencyOrNull(): String? {
        return if (isUrgencyValid()) normalizedUrgencyValue() else null
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "title" to title,
            "description" to description,
            "urgency" to urgency,
            "cover" to cover,
            "isDone" to isDone,
        )
    }

    fun toEntity(): Todo {
        return Todo(
            userId = userId,
            title = title,
            description = description,
            urgency = normalizedUrgencyOrNull() ?: "LOW",
            cover = cover,
            isDone = isDone ?: false,
            updatedAt = Clock.System.now()
        )
    }

}
