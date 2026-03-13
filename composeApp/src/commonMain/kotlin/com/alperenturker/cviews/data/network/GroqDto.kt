package com.alperenturker.cviews.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroqChatRequest(
    val model: String = "llama-3.3-70b-versatile",
    val messages: List<GroqMessage>,
    val max_tokens: Int = 4096,
    val temperature: Float = 0.3f,
)

@Serializable
data class GroqMessage(
    val role: String,
    val content: String,
)

@Serializable
data class GroqChatResponse(
    val choices: List<GroqChoice>? = null,
    val error: GroqError? = null,
)

@Serializable
data class GroqChoice(
    val message: GroqMessage? = null,
)

@Serializable
data class GroqError(
    val message: String? = null,
)
