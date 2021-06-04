package com.saned.view.ui.interfaces
import android.R
import com.saned.model.ApiService.Companion.create
import com.saned.view.ui.fragment.dashboard.MyDashboardFragment
import com.saned.view.ui.fragment.dashboard.WorkItemsFragment
import com.saned.view.ui.fragment.employees.PersonalFragment
import com.saned.view.ui.fragment.employees.TeamFragment


interface ResourceStore {
    companion object {
        val tabList = listOf(
                "My Dashboard", "Work Items"
        )
        val pagerFragments = listOf(
                MyDashboardFragment.create(), WorkItemsFragment.create()
        )
        val tabList2 = listOf(
            "Personal Information", "My Team"
        )
        val fragments = listOf(
            PersonalFragment.create(), TeamFragment.create()
        )
    }
}
