package com.example.languagetranslator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class MyAdapter(var userList : ArrayList<Datalist>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tmessage: TextView = itemView.findViewById(R.id.sourceLanguageEt)
        val tlangto: TextView = itemView.findViewById(R.id.sourceLanguageChooseBtn)
        val tlangfrom: TextView = itemView.findViewById(R.id.targetLanguageChooseBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.tmessage.text = currentItem.message
        holder.tlangfrom.text = currentItem.langfrom
        holder.tlangto.text = currentItem.langto

    }
}