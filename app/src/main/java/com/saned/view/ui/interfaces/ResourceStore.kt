package com.saned.view.ui.interfaces
import com.saned.view.ui.fragment.dashboard.MyDashboardFragment
import com.saned.view.ui.fragment.dashboard.WorkItemsFragment

interface ResourceStore {
    companion object {
        val tabList = listOf(
            "My Dashboard", "Work Items"
        )
        val pagerFragments = listOf(
            MyDashboardFragment.create(), WorkItemsFragment.create()
        )
    }
}
