package com.saned.view.ui.adapter.myEmployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.Empdata
import com.saned.view.ui.activities.MyEmployeesActivity
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*
import kotlinx.android.synthetic.main.employee_list_item.view.*


class MyEmployeesAdapter(private val dataList: List<Empdata>, val context: Context,
                         val activity: MyEmployeesActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemFlag: Int = 0
    var expandable:Boolean=false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RecyclerView.ViewHolder
        if (itemFlag == 0){
            view =
                EmptyHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.empty_placeholder_item, parent, false)
                )
        }else {
            view = ItemViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.employee_list_item, parent, false)
            )
        }
        return view
    }

    override fun getItemCount(): Int {
        itemFlag = dataList.size
        return if (dataList.size > 0) dataList.size else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(itemFlag != 0){
            val viewHolder: ItemViewHolder = (holder as ItemViewHolder)
            viewHolder.nameItem!!.text = "" + dataList[position].f_name
            viewHolder.designationItem!!.text = dataList.get(position).job_title
            viewHolder.emailItem!!.text = dataList.get(position).email
            viewHolder.expandteam
            viewHolder.expandperson
            viewHolder.expandTeamSearch
            viewHolder.expandDash

            viewHolder.expandViewup.setOnClickListener {

                holder.expandlayout.visibility = View.GONE
                holder.expandViewdown.visibility = View.VISIBLE
                holder.expandViewup.visibility = View.GONE
            }

            holder.expandViewup.setOnClickListener {

                holder.expandlayout.visibility = View.GONE
                holder.expandViewdown.visibility = View.VISIBLE
                holder.expandViewup.visibility = View.GONE

            }

            viewHolder.expandViewdown.setOnClickListener {
                holder.expandlayout.visibility = View.VISIBLE
                holder.expandViewdown.visibility = View.GONE
                holder.expandViewup.visibility = View.VISIBLE
            }

            holder.expandViewdown.setOnClickListener {

                holder.expandlayout.visibility = View.VISIBLE
                holder.expandViewdown.visibility = View.GONE
                holder.expandViewup.visibility = View.VISIBLE

            }

            Glide.with(activity)
                .load("http://40.123.199.239:3000/images/" + dataList[position].profile_pic)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(viewHolder.notificationProfile)


        }else if(itemFlag == 0) {

        }

    }

    internal inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nameItem = itemView.name
        val designationItem = itemView.designation
        val emailItem = itemView.email
        val notificationProfile = itemView.profile_image


        val expandViewdown = itemView.expand_items
        val expandViewup = itemView.expand_items_two

        val expandperson = itemView.expand_items2
        val expandteam = itemView.expand_items3

        val expandDash = itemView.expand_items4

        val expandTeamSearch = itemView.expand_items5

        var expandlayout = itemView.expand_view_layout


        init {
            itemView.setOnClickListener {
                activity.onListItemClicked(dataList[adapterPosition],adapterPosition)
            }
        }
    }

    class EmptyHolder(view : View) : RecyclerView.ViewHolder(view){
        val emptyImage = itemView.empty_imageView
        val emptyText = itemView.empty_textView
    }

    interface ListAdapterListener{
        fun onListItemClicked(dummyData: Empdata, position: Int)
    }



}