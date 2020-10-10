package edu.bluejack20_1.learn_ezo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_nav.*

class MenuFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {

        when(position) {
            0 -> {
                return Home()
            }
            1 -> {
                return Practice()
            }
            2 -> {
                return Game()
            }
            3 -> {
                return User()
            }
            else -> {
                return Setting()
            }
        }
    }

}