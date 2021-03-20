package com.saned.view.ui.adapter.servicesActions

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nchores.user.model.ServicesMenu
import com.saned.R
import com.saned.view.ui.activities.ServicesActionsActivity
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*
import kotlinx.android.synthetic.main.services_actions_menu_item.view.*
import org.jetbrains.anko.backgroundColor


class ServiceActionsAdapter(private val dataList: List<ServicesMenu>, val context: Context,
                            val activity: ServicesActionsActivity
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
            view = ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.services_actions_menu_item, parent, false)
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
            val viewHolder: ViewHolder = (holder as ViewHolder)



            viewHolder.itemTitle!!.text = dataList[position].title

        }else if(itemFlag == 0) {
            val emptyViewHolder: EmptyHolder = (holder as EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }

    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val itemTitle = itemView.title

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
        fun onListItemClicked(dummyData: ServicesMenu, position: Int)
    }



}