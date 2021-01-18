package com.example.tabbar

import android.app.PendingIntent.getActivity
import android.content.SharedPreferences
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.tabbar.Adapter.PagerViewAdapter
import com.example.tabbar.Fragments.ListFragment
import com.example.tabbar.Fragments.introduction
import com.example.tabbar.Fragments.ui.login.LoginFragment
import com.example.tabbar.Fragments.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.act
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var dialog: OnBoardingDialog? = null
    private lateinit var calendarBtn:ImageButton
    private lateinit var visualBtn:ImageButton
    private lateinit var homeBtn:ImageButton


    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerViewAdapter: PagerViewAdapter
    private lateinit var image: ImageView


    private var jsonURL = "https://localhost:44386/api/user"


    fun showOnBoardingDialog() {
        dialog?.show(supportFragmentManager, "dialog_fragment")
    }

    fun dismissOnBoardingDialog() {
        dialog?.let {
                dialog ->
            dialog.dismiss()
            this@MainActivity.dialog = null
        }
    }

    fun RemoveAllActivites(editor:SharedPreferences.Editor) {

        for (i in 1..50) {
            try{
                editor.remove(i.toString())
                editor.apply()
            }
            catch(e:java.lang.Exception){}

        }
        editor.remove("poeni1")
        editor.remove("poeni2")
        editor.remove("poeni3")
        editor.remove("poeni4")
        editor.remove("poeni5")
    }

    fun AddAllActivites(editor: SharedPreferences.Editor) {

        //fiziologija
        editor.putString("1","Sedam ili više sati sna.")
        editor.putString("2","Doručak.")
        editor.putString("3","Ručak.")
        editor.putString("4","Večera.")
        editor.putString("5","Toalet?")
        editor.putString("6","Dva ili više litra vode.")
        editor.putString("7","Sit libido.")


        //sigurnost
        editor.putString("11","Stabilan radni odnos/ Mogućnost školovanja.")
        editor.putString("12","Finansijska pokrivenost.")
        editor.putString("13","Zadovoljavajuće zdravstveno stanje.")
        editor.putString("14","Krov nad glavom, dom.")

        //pripadanje
        editor.putString("21","Kontaktirati prijatelja.")
        editor.putString("22","Kontakt sa porodicom.")
        editor.putString("23","Partnerski odnos.")

        //postovanje
        editor.putString("31","Osetiti samouverenost.")
        editor.putString("32","Doživljaj poštovanja od okoline.")

        //samo-aktuelizacija
        editor.putString("41","Rad na dostizanju svog punog potencijala. Biti potpuno ‘ostvaren’.")

        editor.putInt("level",0)

        //poeni
        editor.putInt("poeni1",0)
        editor.putInt("poeni2",0)
        editor.putInt("poeni3",0)
        editor.putInt("poeni4",0)
        editor.putInt("poeni5",0)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val pref = applicationContext.getSharedPreferences("MyPref", 0)
        val editor : SharedPreferences.Editor
        editor = pref.edit()
        if(pref.getString("prviput",null)==null) {
                dialog = OnBoardingDialog().apply {
                    addFragment(introduction.newInstance(
                        this@MainActivity,
                        "pyramid.json",
                        " „Ono što je neophodno za promenu čoveka je promena svesti o samom sebi. “ - Abraham Maslov " + "\n\n" +"Maslovljeva hijerarhija potreba je motivaciona teorija u psihologiji koja se sastoji od petostepenog modela ljudskih potreba."
                                ))

                    addFragment(introduction.newInstance(
                        this@MainActivity,
                        "checklist.json",
                         "Potrebe niže u hijerarhiji moraju biti zadovoljene pre nego što pojedinac može da odgovori na potrebe viših nivoa."+"\n\n" + "Od dna hijerarhije nagore, potrebe su: " + "\n\n"  + "fiziološke potrebe" + "\n\n" + "potrebe bezbednosti"+"\n\n" + "potrebe za ljubavlju i pripadnošću"
                        +"\n\n" + "potrebe poštovanja" + "\n\n" + "samoaktualizacija"))
                    addFragment(introduction.newInstance(
                        this@MainActivity,
                        "growthhelp.json",
                        "Svaka osoba je sposobna i ima želju da napreduje na hijararhiji ka nivou samoaktualizacije. Nažalost, napredak je često ometen neuspehom u zadovoljavanju potreba nižeg nivoa." + "\n\n" + "Ova aplikacija će vam pomoći da pratite kojih dana su vaše potrebe zadovoljene, ali i koji nivoi su bili problematični (oni čije potrebe nisu zadovoljene)."))
                    addFragment(introduction.newInstance(
                        this@MainActivity,
                        "happy.json",

                      "Cilj je unapređenje stila života i dostizanje blagostanja. Praćenjem saznajemo šta nam konkretno nedostaje." + "\n\n" + " „Formulacija problema je često bitnija od njegovog rešenja, što može biti samo stvar matematičke ili eksperimentalne veštine“ - Albert Ajnštajn" ))
                    addFragment(LoginFragment())
                    finishCallback = this@MainActivity::dismissOnBoardingDialog
                    skipCallback = {selectPage(5)}
                    isCancelable = false
                }
                showOnBoardingDialog()
            }
        //Dakle prvo proverit dan, da li je danas ili novi dan.
        // Ako je novi onda ispisi aktivnosti! I ako je null ..postavi!



        //ako je prvi put pokrenuta app
        if(pref.getString("now",null) == null)
        {
            val date = Date()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate:String = sdf.format(date)
            editor.putString("now",currentDate)
            editor.apply()
            //DODaj aktivnosti
            AddAllActivites(editor)

            //Comit
            editor.apply()

        }

        var converted: Date
        var local:Date

        //Uzmi iz Shared Preferences i lokalni datum
        if((pref.getString("now",null) != null)
            ) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {

                val now = pref.getString("now", null)
                converted = sdf.parse(now)

                val date = Date()
                val currentDate:String = sdf.format(date)
                local= sdf.parse(currentDate)

                //ako je novi dan
                if(converted<local) {
                    editor.putString("now",currentDate)
                    RemoveAllActivites(editor)
                    AddAllActivites(editor)
                    //DO SHIT , ReADD activites, Adjust level to 1,
                }
            }
            catch(e:Exception) {e.printStackTrace()  }

        }



        mViewPager = findViewById(R.id.mViewPager)

        // init image buttons

        calendarBtn = findViewById(R.id.calendarBtn)
        visualBtn = findViewById(R.id.visualBtn)
        homeBtn = findViewById(R.id.homeBtn)


        //onclick listener

        calendarBtn.setOnClickListener{
            mViewPager.currentItem =2
        }

        visualBtn.setOnClickListener{
            mViewPager.currentItem =1
        }

        homeBtn.setOnClickListener{
            mViewPager.currentItem =0
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

            calendarBtn.setImageResource(R.drawable.ic_timeline_black)
            visualBtn.setImageResource(R.drawable.ic_star_half_black)
            homeBtn.setImageResource(R.drawable.ic_list_check_pink)

        }

        if(position == 1){

            calendarBtn.setImageResource(R.drawable.ic_timeline_black)
            visualBtn.setImageResource(R.drawable.ic_star_half_pink)
            homeBtn.setImageResource(R.drawable.ic_list_check_black)


        }

        if(position == 2){

            calendarBtn.setImageResource(R.drawable.ic_timeline_pink)
            visualBtn.setImageResource(R.drawable.ic_star_half_black)
            homeBtn.setImageResource(R.drawable.ic_list_check_black)


        }



    }


}


