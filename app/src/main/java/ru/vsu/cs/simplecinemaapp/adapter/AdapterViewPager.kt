package ru.vsu.cs.simplecinemaapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPager(fragmentActivity: FragmentActivity, private val arr: ArrayList<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    private val pageIds = arr.map { it.hashCode().toLong() }
    override fun createFragment(position: Int): Fragment {
        return arr[position]
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun getItemId(position: Int): Long {
        return arr[position].hashCode().toLong() // make sure notifyDataSetChanged() works
    }

    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
    }
}
