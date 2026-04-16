
package org.ifs23017.data

import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null
)