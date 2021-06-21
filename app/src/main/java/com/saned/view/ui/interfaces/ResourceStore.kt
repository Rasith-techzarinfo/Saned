package com.saned.view.ui.interfaces
import com.saned.R
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
    val tabIcons = listOf(
            R.drawable.ic_baseline_bar_chart_24,R.drawable.ic_baseline_list_alt_24
    )
    val tabList2 = listOf(
      "Personal", "My Team"
    )
    val fragments = listOf(
      PersonalFragment.create(), TeamFragment.create()
    )
    val tabIcons2 = listOf(
            R.drawable.ic_baseline_person,R.drawable.ic_baseline_groups_24
    )
  }
}
