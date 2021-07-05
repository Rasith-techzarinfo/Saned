package com.saned.view.ui.fragment.adapterfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.Data
import com.saned.view.ui.activities.PendingRequestsActivity
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.dynamic_wf_list_item.view.*
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*
import kotlinx.android.synthetic.main.work_items_show.view.*

class WorkItemsAdapter (private val dataList: List<Data>, val context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemFlag: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: RecyclerView.ViewHolder
        if (itemFlag == 0){
            view =
                    WorkItemsAdapter.EmptyHolder(
                            LayoutInflater.from(context)
                                    .inflate(R.layout.empty_placeholder_item, parent, false)
                    )
        }else {
            view = ViewHolder(
                    LayoutInflater.from(context)
                            .inflate(R.layout.work_items_show, parent, false)
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
            val viewHolder: WorkItemsAdapter.ViewHolder = (holder as ViewHolder)

            viewHolder.itemVal1!!.text = "" + dataList[position].form_name
            viewHolder.itemVal2!!.text = "" + dataList[position].added_by
            viewHolder.itemVal3!!.text = "" + dataList[position].status
            viewHolder.itemval4!!.text = Utils.convertDbtoNormalDateTime1("" + dataList[position].added_at )

            Glide.with(context)
                .load("http://40.123.199.239:3000/images/" + dataList[position].profile)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(viewHolder.itemVal5)


        }else if(itemFlag == 0) {
            val emptyViewHolder: WorkItemsAdapter.EmptyHolder = (holder as EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }
    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val itemVal1 = itemView.name
        val itemVal2 = itemView.designation
        val itemVal3 = itemView.email
        val itemval4 = itemView.date
        val itemVal5 = itemView.profile_image

        init {
            itemView.setOnClickListener {
               // activity.onListItemClicked(dataList[adapterPosition],adapterPosition)
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

