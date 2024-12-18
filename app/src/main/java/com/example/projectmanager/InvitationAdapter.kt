package com.example.projectmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InvitationAdapter(
    private val invitationList: List<Invitation>,
    private val userList: List<User> // Lista de usuarios para obtener los nombres
) : RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder>() {

    class InvitationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val invitationStatus: TextView = view.findViewById(R.id.invitationStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.invitation_item, parent, false)
        return InvitationViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        val invitation = invitationList[position]
        val user = userList.find { it.id == invitation.id_user }

        holder.userName.text = user?.nombre ?: "Usuario desconocido"
        holder.invitationStatus.text = invitation.status
    }

    override fun getItemCount(): Int {
        return invitationList.size
    }
}
