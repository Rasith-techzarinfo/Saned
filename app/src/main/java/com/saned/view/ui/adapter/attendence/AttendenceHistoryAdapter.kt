package com.saned.view.ui.adapter.attendence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.AttenHistoryDetail
import com.saned.model.AttendenceData
import com.saned.view.ui.activities.MyEmployeesActivity
import com.saned.view.ui.activities.NotificationActivity
import com.saned.view.ui.activities.attendence.AttendanceHistoryActivity
import kotlinx.android.synthetic.main.attendence_history_item.view.*
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*


class AttendenceHistoryAdapter(private val dataList: List<AttenHistoryDetail>, val context: Context,
                               val activity: AttendanceHistoryActivity
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
                            .inflate(R.layout.attendence_history_item, parent, false)
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
            viewHolder.dateItem!!.text = dataList.get(position).date
            viewHolder.timeInItem!!.text = dataList.get(position).in_time
            viewHolder.timeOutItem!!.text = dataList.get(position).out_time
            viewHolder.workingHrsItem!!.text = dataList.get(position).working_hours


        }else if(itemFlag == 0) {
            val emptyViewHolder: EmptyHolder = (holder as EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }

    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val dateItem = itemView.date
        val timeInItem = itemView.timeIN
        val timeOutItem = itemView.timeOUT
        val workingHrsItem = itemView.wokringHrs

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
        fun onListItemClicked(dummyData: AttenHistoryDetail, position: Int)
    }



}