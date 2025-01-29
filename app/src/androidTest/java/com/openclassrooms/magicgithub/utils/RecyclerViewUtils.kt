package com.openclassrooms.magicgithub.utils

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.openclassrooms.magicgithub.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert

object RecyclerViewUtils {
    @JvmStatic
    fun clickChildView(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.withId(R.id.activity_list_user_fab)
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }


    class ItemCount(private val expectedCount: Int) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            Assert.assertNotNull(adapter)
            Assert.assertEquals(adapter!!.itemCount.toLong(), expectedCount.toLong())
        }
    }
    // 🔥 **Ajout du matcher pour vérifier la couleur de fond**
    @JvmStatic
    fun hasBackgroundColor(expectedColor: Int): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with background color: $expectedColor")
            }

            override fun matchesSafely(view: View): Boolean {
                val background = view.background
                if (background is ColorDrawable) {
                    return background.color == expectedColor
                }
                return false
            }
        }
    }

    // 🔥 **Ajout du matcher pour vérifier un élément spécifique dans la RecyclerView**
    @JvmStatic
    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("item at position $position ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    ?: return false // Si l'élément n'est pas visible
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
    @JvmStatic
    fun dragAndDrop(fromPosition: Int, toPosition: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(RecyclerView::class.java)
            }

            override fun getDescription(): String {
                return "Drag and drop item from position $fromPosition to $toPosition"
            }

            override fun perform(uiController: UiController, view: View) {
                val recyclerView = view as RecyclerView
                val fromViewHolder = recyclerView.findViewHolderForAdapterPosition(fromPosition)
                val toViewHolder = recyclerView.findViewHolderForAdapterPosition(toPosition)

                if (fromViewHolder != null && toViewHolder != null) {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                    uiController.loopMainThreadUntilIdle()
                }
            }
        }
    }

}