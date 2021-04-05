package com.saned.view.ui.adapter.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.NotifyData
import com.saned.view.ui.activities.NotificationActivity
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*
import kotlinx.android.synthetic.main.notification_list_item.view.*


class NotificationAdapter(private val dataList: List<NotifyData>, val context: Context,
                          val activity: NotificationActivity
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
                            .inflate(R.layout.notification_list_item, parent, false)
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
            viewHolder.notificationTitle!!.text = dataList.get(position).title
            viewHolder.notificationDescription!!.text =
                    dataList.get(position).message
            viewHolder.notificationAgo!!.text = dataList.get(position).timeAgo

            Glide.with(activity)
                    .load(dataList[position].profile)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(viewHolder.notificationProfile)

            if (dataList[position].readStatus=="0"){


                viewHolder.bgLayout.setBackgroundColor(context.resources.getColor(R.color.notify_unread_color))
            }else{

                viewHolder.bgLayout.setBackgroundColor(context.resources.getColor(R.color.notify_read_color))
            }


        }else if(itemFlag == 0) {
            val emptyViewHolder: EmptyHolder = (holder as EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }

    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val notificationTitle = itemView.title_text
        val notificationDescription = itemView.description
        val notificationAgo = itemView.time_ago
        val rootLayout = itemView.notify_card
        val bgLayout = itemView.bg_layout
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
        fun onListItemClicked(dummyData: NotifyData, position: Int)
    }



}