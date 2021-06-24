package com.saned.view.ui.adapter.pendingDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saned.R
import com.saned.model.Data
import com.saned.model.PendingDetail
import com.saned.view.ui.activities.PendingDetailActivity
import com.saned.view.ui.activities.PendingRequestsActivity
import kotlinx.android.synthetic.main.dynamic_wf_list_item.view.*
import kotlinx.android.synthetic.main.dynamic_wf_list_item.view.labelVal2
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*
import kotlinx.android.synthetic.main.pending_detail_items.view.*


class PendingDetailAdapter(private val dataList: List<PendingDetail>, val context: Context,
                           val activity: PendingDetailActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemFlag: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: RecyclerView.ViewHolder
        if (itemFlag == 0){
            view =
                PendingDetailAdapter.EmptyHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.empty_placeholder_item, parent, false)
                )
        }else {
            view = ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.pending_detail_items, parent, false)
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
            val viewHolder: PendingDetailAdapter.ViewHolder = (holder as PendingDetailAdapter.ViewHolder)

//            viewHolder.itemVal1!!.text = "" + dataList[position].f_name
//            viewHolder.itemVal2!!.text = "" + dataList[position].emp_code
//            viewHolder.itemVal3!!.text = "" + dataList[position].join
//            viewHolder.itemVal4!!.text = "" + dataList[position].basic
//            viewHolder.itemVal5!!.text = "" + dataList[position].cnttyp
//            viewHolder.itemVal6!!.text = "" + dataList[position].bank
//            viewHolder.itemVal7!!.text = "" + dataList[position].a_name
//            viewHolder.itemVal8!!.text = "" + dataList[position].pspt
//            viewHolder.itemVal9!!.text = "" + dataList[position].email
//            viewHolder.itemVal10!!.text = "" + dataList[position].phon
//            viewHolder.itemVal11!!.text = "" + dataList[position].mart
//            viewHolder.itemVal12!!.text = "" + dataList[position].city
//            viewHolder.itemVal13!!.text = "" + dataList[position].gend
//            viewHolder.itemVal14!!.text = "" + dataList[position].relg



        }else if(itemFlag == 0) {
            val emptyViewHolder: PendingDetailAdapter.EmptyHolder = (holder as PendingDetailAdapter.EmptyHolder)

//            emptyViewHolder.emptyImage.setImageResource(R.drawable.ic_notification)
            emptyViewHolder.emptyText.text = "No Data Found"

        }
    }

    internal inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

//        val itemVal1 = itemView.labelVal15
//        val itemVal2 = itemView.labelVal16
//        val itemVal3 = itemView.labelVal3
//        val itemVal4 = itemView.labelVal4
//        val itemVal5 = itemView.labelVal5
//        val itemVal6 = itemView.labelVal6
//        val itemVal7 = itemView.labelVal7
//        val itemVal8 = itemView.labelVal8
//        val itemVal9 = itemView.labelVal9
//        val itemVal10 = itemView.labelVal10
//        val itemVal11 = itemView.labelVal11
//        val itemVal12 = itemView.labelVal12
//        val itemVal13 = itemView.labelVal13
//        val itemVal14 = itemView.labelVal14

        init {
            itemView.setOnClickListener {
              //  activity.onListItemClicked(dataList[adapterPosition],adapterPosition)
            }
        }
    }

    class EmptyHolder(view : View) : RecyclerView.ViewHolder(view){
        val emptyImage = itemView.empty_imageView
        val emptyText = itemView.empty_textView
    }

    interface ListAdapterListener{
        fun onListItemClicked(dummyData: PendingDetail, position: Int)
    }
}