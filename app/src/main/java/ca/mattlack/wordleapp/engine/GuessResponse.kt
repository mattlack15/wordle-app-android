package ca.mattlack.wordleapp.engine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GuessResponse {
    @Serializable
    @SerialName("success")
    class Success(val result: String) : GuessResponse()
    @Serializable
    @SerialName("failure")
    class Failure(val error: String) : GuessResponse()
}