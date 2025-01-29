package com.openclassrooms.magicgithub.ui.user_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.magicgithub.databinding.ItemListUserBinding
import com.openclassrooms.magicgithub.model.User

class UserListAdapter(
    private var users: MutableList<User>,
    private val callback: Listener
) : RecyclerView.Adapter<UserListAdapter.ListUserViewHolder>() {

    inner class ListUserViewHolder(private val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemListUserAvatar)

            binding.itemListUserUsername.text = user.login
            binding.itemListUserDeleteButton.setOnClickListener { callback.onClickDelete(user) }

            // 🔥 **Gestion de l'activation/désactivation**
            binding.root.setBackgroundColor(if (user.isActive) Color.WHITE else Color.RED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val binding = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun getUserAt(position: Int): User = users[position]

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedUser = users.removeAt(fromPosition)
        users.add(toPosition, movedUser)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateList(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClickDelete(user: User)
    }
}
