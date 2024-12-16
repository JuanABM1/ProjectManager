package com.example.projectmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable

class MainPageUsers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page_users)

        val user = intent.getSerializableExtra("user") as User
        val users = intent.getSerializableExtra("users") as ArrayList<User>

        val firstFragment = HomeFragment.newInstance(user, users)
        val secondFragment = TaskFragment.newInstance(user)
        val thirdFragment = ProfileFragment()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.tasks->setCurrentFragment(secondFragment)
                R.id.settings->setCurrentFragment(thirdFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {

            setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_slide_out,
                R.anim.fragment_slide_in_reverse,
                R.anim.fragment_slide_out_reverse
            )

            replace(R.id.flFragment, fragment)
            commit()
        }
    }
}