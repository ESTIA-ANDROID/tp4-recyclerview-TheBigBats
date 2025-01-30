package com.openclassrooms.magicgithub.repository

import com.openclassrooms.magicgithub.model.User

interface UserRepository {
    fun getUsers(): List<User>
    fun addRandomUser()
    fun deleteUser(user: User)
}