package com.voyantiq.app.data.repository

import com.voyantiq.app.data.model.User
import com.voyantiq.app.data.model.UserPreferences
import java.util.UUID

class UserRepository {
    // Store users in memory for now (we'll add database later)
    private val users = mutableMapOf<String, User>()

    suspend fun createUser(
        email: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): Result<User> {
        return try {
            val user = User(
                id = UUID.randomUUID().toString(),
                email = email,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                preferences = UserPreferences()
            )
            users[user.id] = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            val user = users.values.find { it.email == email }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<User> {
        return try {
            val existingUser = users[userId] ?: throw Exception("User not found")
            val updatedUser = existingUser.copy(preferences = preferences)
            users[userId] = updatedUser
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}