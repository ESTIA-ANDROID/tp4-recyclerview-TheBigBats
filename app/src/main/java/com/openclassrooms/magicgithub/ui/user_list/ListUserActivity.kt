package com.openclassrooms.magicgithub.ui.user_list

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.magicgithub.api.FakeApiService
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.magicgithub.controller.UserController
import com.openclassrooms.magicgithub.databinding.ActivityListUserBinding
import com.openclassrooms.magicgithub.model.User

class ListUserActivity : AppCompatActivity(), UserListAdapter.Listener {
    private lateinit var binding: ActivityListUserBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userController = UserController(FakeApiService())
        configureRecyclerView()
        configureFab()
        setupSwipeActions()
        setupDragAndDrop()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    // 📌 **Configuration RecyclerView avec ViewBinding**
    private fun configureRecyclerView() {
        adapter = UserListAdapter(userController.getUsers().toMutableList(), this)
        binding.activityListUserRv.layoutManager = LinearLayoutManager(this)
        binding.activityListUserRv.adapter = adapter
    }

    // 📌 **Ajout d'un utilisateur aléatoire**
    private fun configureFab() {
        binding.activityListUserFab.setOnClickListener {
            userController.addRandomUser()
            loadData()
        }
    }

    // 📌 **Chargement des données**
    private fun loadData() {
        adapter.updateList(userController.getUsers())
    }

    // 📌 **Swipe pour activer/désactiver un utilisateur**
    private fun setupSwipeActions() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = adapter.getUserAt(position)
                user.isActive = !user.isActive
                adapter.notifyItemChanged(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.activityListUserRv)
    }

    // 📌 **Drag and Drop pour réorganiser la liste**
    private fun setupDragAndDrop() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.moveItem(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        })
        itemTouchHelper.attachToRecyclerView(binding.activityListUserRv)
    }

    // 📌 **Suppression d'un utilisateur**
    override fun onClickDelete(user: User) {
        Log.d(ListUserActivity::class.java.name, "User tries to delete an item.")
        userController.deleteUser(user)
        loadData()
    }
}
