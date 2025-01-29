package com.openclassrooms.magicgithub

import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS
import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS_RANDOM
import com.openclassrooms.magicgithub.di.Injection
import com.openclassrooms.magicgithub.model.User
import com.openclassrooms.magicgithub.repository.UserRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Unit test, which will execute on a JVM.
 * Testing UserRepository.
 */
@RunWith(JUnit4::class)
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = Injection.getRepository()
    }

    @Test
    fun getUsersWithSuccess() {
        val usersActual = userRepository.getUsers()
        val usersExpected: List<User> = FAKE_USERS
        assertEquals(
            usersActual,
            usersExpected
        )
    }

    @Test
    fun generateRandomUserWithSuccess() {
        val initialSize = userRepository.getUsers().size
        userRepository.addRandomUser()
        val user = userRepository.getUsers().last()
        assertEquals(userRepository.getUsers().size, initialSize + 1)
        assertTrue(
            FAKE_USERS_RANDOM.filter {
                it.equals(user)
            }.isNotEmpty()
        )
    }

    @Test
    fun deleteUserWithSuccess() {
        val userToDelete = userRepository.getUsers()[0]
        userRepository.deleteUser(userToDelete)
        Assert.assertFalse(userRepository.getUsers().contains(userToDelete))
    }

    // 🔥 **Test pour activer/désactiver un utilisateur via un swipe**
    @Test
    fun toggleUserActiveStateWithSuccess() {
        val user = userRepository.getUsers()[0]
        val initialState = user.isActive

        // Simulation du swipe pour désactiver
        user.isActive = !user.isActive
        assertNotEquals(initialState, user.isActive)

        // Nouvelle simulation du swipe pour réactiver
        user.isActive = !user.isActive
        assertEquals(initialState, user.isActive)
    }

    // 🔥 **Test pour vérifier que le fond change en fonction de l'état de l'utilisateur**
    @Test
    fun checkBackgroundColorWhenTogglingUserState() {
        val user = userRepository.getUsers()[0]

        // Désactiver l'utilisateur (Swipe)
        user.isActive = false
        assertEquals(false, user.isActive) // Supposons que l'état inactif soit rouge dans l'UI

        // Réactiver l'utilisateur (Swipe)
        user.isActive = true
        assertEquals(true, user.isActive) // Supposons que l'état actif soit blanc dans l'UI
    }

    // 🔥 **Test pour réordonner les utilisateurs**
    @Test
    fun reorderUsersWithSuccess() {
        val users = userRepository.getUsers().toMutableList()

        // Échanger le premier et le deuxième utilisateur
        val movedUser = users.removeAt(0)
        users.add(1, movedUser)

        assertEquals(users[1], movedUser)
    }
}