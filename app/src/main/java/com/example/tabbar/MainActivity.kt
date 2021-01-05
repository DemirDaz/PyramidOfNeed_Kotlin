package com.example.tabbar

import android.os.Bundle
import android.widget.Adapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.tabbar.Adapter.PagerViewAdapter


class MainActivity : AppCompatActivity() {


    private lateinit var calendarBtn:ImageButton
    private lateinit var visualBtn:ImageButton
    private lateinit var homeBtn:ImageButton

    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerViewAdapter: PagerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init views 33min

        mViewPager = findViewById(R.id.mViewPager)

        // init image buttons

        calendarBtn = findViewById(R.id.calendarBtn)
        visualBtn = findViewById(R.id.visualBtn)
        homeBtn = findViewById(R.id.homeBtn)


        //onclick listener

        calendarBtn.setOnClickListener{
            mViewPager.currentItem =0
        }

        visualBtn.setOnClickListener{
            mViewPager.currentItem =1
        }

        homeBtn.setOnClickListener{
            mViewPager.currentItem =2
        }

        mPagerViewAdapter = PagerViewAdapter(supportFragmentManager)
        mViewPager.adapter = mPagerViewAdapter
        mViewPager.offscreenPageLimit = 3

        // add page change listener

        mViewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                changingTabs(position)
            }
        })

        //default page
        mViewPager.currentItem = 1
        visualBtn.setImageResource(R.drawable.ic_star_half_pink)

    }

    private fun changingTabs(position: Int) {

        if(position == 0){

            calendarBtn.setImageResource(R.drawable.ic_timeline_pink)
            visualBtn.setImageResource(R.drawable.ic_star_half_black)
            homeBtn.setImageResource(R.drawable.ic_list_check_black)

        }

        if(position == 1){

            calendarBtn.setImageResource(R.drawable.ic_timeline_black)
            visualBtn.setImageResource(R.drawable.ic_star_half_pink)
            homeBtn.setImageResource(R.drawable.ic_list_check_black)

        }

        if(position == 2){

            calendarBtn.setImageResource(R.drawable.ic_timeline_black)
            visualBtn.setImageResource(R.drawable.ic_star_half_black)
            homeBtn.setImageResource(R.drawable.ic_list_check_pink)

        }

    }
}
