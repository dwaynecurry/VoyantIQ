package com.voyantiq.app.data.auth

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut()
    fun isUserAuthenticated(): Boolean
    fun getCurrentUserId(): String?
}