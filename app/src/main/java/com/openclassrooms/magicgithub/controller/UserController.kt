package com.openclassrooms.magicgithub.controller

import com.openclassrooms.magicgithub.model.User
import com.openclassrooms.magicgithub.repository.UserRepository

class UserController(private val repository: UserRepository) {

    fun getUsers(): List<User> = repository.getUsers()

    fun addRandomUser() {
        repository.addRandomUser()
    }

    fun deleteUser(user: User) {
        repository.deleteUser(user)
    }
}
