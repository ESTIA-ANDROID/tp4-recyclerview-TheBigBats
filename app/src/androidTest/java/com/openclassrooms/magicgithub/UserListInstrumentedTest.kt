package com.openclassrooms.magicgithub

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.openclassrooms.magicgithub.api.FakeApiService
import com.openclassrooms.magicgithub.controller.UserController
import com.openclassrooms.magicgithub.ui.user_list.ListUserActivity
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.ItemCount
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.clickChildView
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.hasBackgroundColor
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.atPosition
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.dragAndDrop

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 * Testing ListUserActivity screen.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class UserListInstrumentedTest {
    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(ListUserActivity::class.java)
    private lateinit var userController: UserController
    private var currentUsersSize = -1

    @Before
    fun setup() {
        userController = UserController(FakeApiService())
        currentUsersSize = userController.getUsers().size
    }

    @Test
    fun checkIfRecyclerViewIsNotEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize))
    }

    @Test
    fun checkIfAddingRandomUserIsWorking() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_fab))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize + 1))
    }

    @Test
    fun checkIfRemovingUserIsWorking() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildView(R.id.item_list_user_delete_button)
                )
            )
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize - 1))
    }

    // 📌 **Vérifie que le swipe active/désactive un utilisateur**
    @Test
    fun checkIfUserActivationIsWorking() {
        // Swipe l'utilisateur en position 0
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.swipeLeft()))

        // Vérifie si la cellule est bien en rouge (désactivée)
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(matches(atPosition(0, hasBackgroundColor(android.graphics.Color.RED))))

        // Swipe à nouveau pour réactiver
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.swipeRight()))

        // Vérifie si la cellule est revenue en blanc (active)
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(matches(atPosition(0, hasBackgroundColor(android.graphics.Color.WHITE))))
    }



}
