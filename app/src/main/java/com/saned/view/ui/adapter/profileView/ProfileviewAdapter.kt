package com.saned.view.ui.adapter.profileView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.Empdata
import com.saned.model.Profile
import com.saned.view.ui.activities.MyEmployeesActivity
import com.saned.view.ui.activities.ProfileActivity
import kotlinx.android.synthetic.main.activity_profile_view.view.*
import kotlinx.android.synthetic.main.employee_list_item.view.*
import kotlinx.android.synthetic.main.empty_placeholder_item.view.*


class ProfileviewAdapter(private val dataList: List<Profile>, val context: Context,
                         val activity: ProfileActivity
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
                            .inflate(R.layout.activity_profile_view, parent, false)
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


            viewHolder.namef!!.text = "" + dataList[position].fnme
            viewHolder.lastf!!.text = "" + dataList[position].lnme
            viewHolder.email!!.text = "" + dataList[position].email
            viewHolder.middle!!.text = "" + dataList[position].mnme
            viewHolder.arab!!.text = "" + dataList[position].a_name
            viewHolder.dob!!.text = "" + dataList[position].dob
            viewHolder.nationlity!!.text = "" + dataList[position].ccty
            viewHolder.gender!!.text = "" + dataList[position].gend
            viewHolder.phone!!.text = "" + dataList[position].phon
            viewHolder.religion!!.text = "" + dataList[position].relg
            viewHolder.empcode!!.text = "" + dataList[position].emp_code
            viewHolder.fullname!!.text = "" + dataList[position].f_name
            viewHolder.mart!!.text = "" + dataList[position].mart
            viewHolder.jobtitle!!.text = "" + dataList[position].job_title
            viewHolder.deprtment!!.text = "" + dataList[position].dept
            viewHolder.bsicsalry!!.text = "" + dataList[position].basic
            viewHolder.houseallow!!.text = "" + dataList[position].hous
            viewHolder.city!!.text = "" + dataList[position].city
            viewHolder.loca!!.text = "" + dataList[position].loca
            viewHolder.idn!!.text = "" + dataList[position].id
            viewHolder.lastdate!!.text = "" + dataList[position].ldate
            viewHolder.tran!!.text = "" + dataList[position].tran
            viewHolder.grade!!.text = "" + dataList[position].grade
            viewHolder.cont!!.text = "" + dataList[position].cont
            viewHolder.medc!!.text = "" + dataList[position].medc
            viewHolder.idexpry!!.text = "" + dataList[position].idex
            viewHolder.pssno!!.text = "" + dataList[position].pspt
            viewHolder.passexpry!!.text = "" + dataList[position].psptex
            viewHolder.subdepartment!!.text = "" + dataList[position].subdep
            viewHolder.idnum!!.text = "" + dataList[position].idno





//            Glide.with(activity)
//                    .load(dataList[position].profile_pic)
//                    .placeholder(R.drawable.ic_user)
//                    .error(R.drawable.ic_user)
//                    .into(viewHolder.notificationProfile)


        }else if(itemFlag == 0) {

        }

    }

    internal inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val namef = itemView.firstNameEditText2
        val lastf = itemView.lastNameEditText2
        val email = itemView.emailEditText2
       // val notificationProfile = itemView.profile_image
       val middle = itemView.middleNameEditText2
        val arab = itemView.arabicNameEditText2
        val dob = itemView.dobEditText2
        val nationlity = itemView.nationalityEditText2
        val gender = itemView.genderEditText2
        val phone = itemView.phoneEditText2
        val religion = itemView.religionEditText2
        val empcode = itemView.empcodeEditText2
        val fullname = itemView.fullNameEditText2
        val jobtitle = itemView.jobTitleEditText2
        val deprtment = itemView.departmentEditText2
        val bsicsalry = itemView.basicEditText2
        val houseallow = itemView.housingEditText2
        val idn = itemView.idEditText2
        val lastdate = itemView.lastdateEditText2
        val grade = itemView.gradeEditText2
        val idexpry = itemView.idexpiryEditText2
        val pssno = itemView.passEditText2
        val passexpry = itemView.passexpiryEditText2
        val subdepartment = itemView.subDeptEditText2
        val idnum = itemView.idnumEditText2
        val mart = itemView.maritialEditText2
        val city = itemView.cityEditText2
        val loca = itemView.locationEditText2
        val tran = itemView.transportEditText2
        val cont = itemView.contractstsEditText2
        val medc = itemView.medicalEditText2





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
        fun onListItemClicked(dummyData: Profile, position: Int)
    }



}