package com.saned.view.ui.design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saned.R
import com.saned.view.ui.activities.ServicesActionsActivity
import com.saned.view.ui.activities.dynamicWF.CreateDynamicWFActivity
import kotlinx.android.synthetic.main.modal_bottom_sheet.*

class ModalBottomSheet (val activity: CreateDynamicWFActivity
) : BottomSheetDialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //listeners
        gallery_layout.setOnClickListener {
            activity.addPhotoVideoFromDevice()
            dismiss()
        }

        document_layout.setOnClickListener {
            activity.openDocuments()
            dismiss()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    interface OnOptionCLickListener{
        fun onManageClick1()
        fun onManageClick2()
    }
}