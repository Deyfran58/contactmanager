package Internet

data class RespAp<T>(
    val data: T?,
    val responseCode: Int,
    val message: String
)