package com.example.tabbar

import android.animation.ArgbEvaluator
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.dialog_onboarding.*

class OnBoardingDialog : DialogFragment() , OnPageChangeListener{


    private var startColor : Int? = null
    private var endColor : Int? = null
    private var pagerAdapter : FragmentPagerAdapter? = null

    private val fragmentList = mutableListOf<Fragment>()
    private val indicatorList = mutableListOf<ImageView>()
    private val argbEvaluator =  ArgbEvaluator()

    var finishCallback : (()-> Unit)? = null
    var skipCallback : (()-> Unit)? = null
    var skipText: String? = null
    var finishText: String? = null


    fun clearFragments() = fragmentList.clear()
    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)
    fun addFragments(fragments : List<Fragment>) =fragmentList.addAll(fragments)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            val v = inflater?.inflate(R.layout.dialog_onboarding, container, false)
            val indicatorContainer = v?.findViewById<LinearLayout>(R.id.indicator_container)
            indicatorContainer?.let {
                if(fragmentList.size > 1)
                    for (i in 0 until fragmentList.size) { addIndicator(indicatorContainer, i) }
            }
            return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startColor = ResourcesCompat.getColor(resources, R.color.white, null)
        endColor  = ResourcesCompat.getColor(resources, R.color.silver, null)
        pagerAdapter = OnBoardingAdapter(childFragmentManager)

        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)
        val editor : SharedPreferences.Editor
        editor = pref.edit()

        view_pager_container?.adapter = pagerAdapter
        view_pager_container?.addOnPageChangeListener(this)
        view_pager_container?.currentItem = 0
        view_pager_container?.setBackgroundResource(R.color.trans_pink)

        intro_btn_skip.setOnClickListener { skipCallback?.invoke()
           }
        skipText?.let { intro_btn_skip.text = it }

        intro_btn_finish.setOnClickListener {
            if((pref.getString("email",null) != null) &&
                (pref.getString("password",null) != null)){
            finishCallback?.invoke()
            editor.putString("prviput","nije")
            editor.apply() }
           }
        finishText?.let { intro_btn_finish.text = it }

        intro_btn_next.setOnClickListener { view_pager_container?.currentItem?.let { view_pager_container?.currentItem = it+1 } }
        onPageSelected(0)
    }

    fun selectPage(position : Int){
        view_pager_container?.currentItem = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position < fragmentList.size - 1 ) {
            view_pager_container?.setBackgroundColor(argbEvaluator.evaluate((position.toFloat() + positionOffset) / fragmentList.size.toFloat(),
                startColor, endColor) as Int)
        }
    }

    override fun onPageSelected(position: Int) {
        intro_btn_next.visibility = if (position+1 == pagerAdapter?.count) View.GONE else View.VISIBLE
        intro_btn_finish.visibility = if (position+1 == pagerAdapter?.count) View.VISIBLE else View.GONE
        intro_btn_skip.visibility = if (position+1 == pagerAdapter?.count) View.GONE else View.VISIBLE
        if(indicatorList.size > 0) {
            indicatorList.forEach { it.setImageResource(R.drawable.indicator_unselected) }
            indicatorList[position].setImageResource(R.drawable.indicator_selected)
        }
    }

    private inner class OnBoardingAdapter(fm : FragmentManager?):FragmentPagerAdapter(fm!!){
        override fun getItem(position: Int): Fragment =
            if (position <fragmentList.size) fragmentList[position] else fragmentList[1] //bilo je else null
        override fun getCount(): Int = fragmentList.size
    }

    private fun  addIndicator (container : LinearLayout, position :Int){
        val indicator = LayoutInflater.from(context).inflate(R.layout.view_indicator, container,false)
        indicatorList.add(position,indicator as ImageView)
        container.addView(indicator)
    }
}