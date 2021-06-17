package com.saned.view.ui.adapter.pendingRequests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saned.view.ui.activities.PendingRequestsActivity
import com.saned.R
import com.saned.model.Data
import kotlinx.android.synthetic.main.dynamic_wf_list_item.view.*
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*

class PendingRequestsAdapter(private val dataList: List<Data>, val context: Context,
                             val activity: PendingRequestsActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemFlag: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: RecyclerView.ViewHolder
        if (itemFlag == 0){
            view =
                    PendingRequestsAdapter.EmptyHolder(
                            LayoutInflater.from(context)
                                    .inflate(R.layout.empty_placeholder_item, parent, false)
                    )
        }else {
            view = ViewHolder(
                    LayoutInflater.from(context)
                            .inflate(R.layout.dynamic_wf_list_item, parent, false)
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
            val viewHolder: PendingRequestsAdapter.ViewHolder = (holder as PendingRequestsAdapter.ViewHolder)

            viewHolder.itemVal1!!.text = "" + dataList[position].job_title
            viewHolder.itemVal2!!.text = "" + dataList[position].status
            viewHolder.itemVal3!!.text = "" + dataList[position].added_by

        }else if(itemFlag == 0) {
            val emptyViewHolder: PendingRequestsAdapter.EmptyHolder = (holder as PendingRequestsAdapter.EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }
    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val itemVal1 = itemView.labelVal1
        val itemVal2 = itemView.labelVal2
        val itemVal3 = itemView.labelVal33

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
        fun onListItemClicked(dummyData: Data, position: Int)
    }
}