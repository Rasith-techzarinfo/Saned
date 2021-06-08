package com.saned.view.ui.adapter.myEmployees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
            viewHolder.nameItem!!.text = "" + dataList[position].t_nama
            viewHolder.designationItem!!.text = dataList.get(position).t_role
            viewHolder.emailItem!!.text = dataList.get(position).t_mail

            Glide.with(activity)
                .load(dataList[position].profile_pic)
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