package com.example.ch7ex


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(private val context: Context, private val repositories: List<RepoD>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        println("onCreateViewHolder")

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("onBindViewHolder")

        val repository = repositories[position]

        holder.itemView.findViewById<TextView>(R.id.textView1).text = repository.name
        holder.itemView.findViewById<TextView>(R.id.textView2).text = repository.owner
        holder.itemView.setOnClickListener {
            AlertDialog.Builder(context).setMessage("You clicked ${repository.name}.").show()
        }
    }

    override fun getItemCount(): Int {
        return repositories.size
    }
}