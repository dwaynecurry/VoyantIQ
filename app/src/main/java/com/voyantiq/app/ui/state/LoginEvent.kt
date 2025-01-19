package com.voyantiq.app.ui.state

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object SignUpClicked : LoginEvent()
    object DismissError : LoginEvent()
}