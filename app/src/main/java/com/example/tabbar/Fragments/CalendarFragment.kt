package com.example.tabbar.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.example.tabbar.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            //val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            //Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
            var date:String = ""
            if(month<9)
             date = year.toString() + "-0" + (month + 1).toString() + "-" + dayOfMonth
            else date= year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth
            var izabrani: LocalDate = LocalDate.parse(date)
            val jsonURL = "http://10.0.2.2:44386/api/calendar/" + "?id=" + pref.getString(
                "email",
                null
            ) + "&date=" + izabrani.toString()
            doAsync {
                var result = URL(jsonURL).readText()
                uiThread {
                    if (result != "null") {
                        var jsoni: JSONObject = JSONObject(result)

                        var poeni1: Any = jsoni.get("C1")
                        var poeni2: Any = jsoni.get("C2")
                        var poeni3: Any = jsoni.get("C3")
                        var poeni4: Any = jsoni.get("C4")
                        var poeni5: Any = jsoni.get("C5")


                        fullfilment.text = "Fullfiled:" + percentage(poeni1.toString().toInt(),poeni2.toString().toInt(),poeni3.toString().toInt(),poeni4.toString().toInt(),poeni5.toString().toInt()).toString() + "%"
                        problematic(poeni1.toString().toInt(),poeni2.toString().toInt(),poeni3.toString().toInt(),poeni4.toString().toInt(),poeni5.toString().toInt())
                        trophy.text = "Trophy: "
                        medal(percentage(poeni1.toString().toInt(),poeni2.toString().toInt(),poeni3.toString().toInt(),poeni4.toString().toInt(),poeni5.toString().toInt()))
                        //trophy add one of 4 images
                    }
                    else {
                        fullfilment.text = "Fullfiled :No record"
                        problematic.text = "Problematic needs: No record"
                        trophy.text = "Medal: No record"
                        lottiecal.visibility =View.GONE

                    }
                }
            }


        }

    }

    fun percentage(first:Int,second: Int,third: Int,fourth: Int,fifth: Int):Int {
        var ukupno = first+second+third+fourth+fifth
        var percentage = (ukupno.toFloat()/17) * 100
        var rounded = percentage.toInt()
        return rounded
    }
    fun medal(percentage:Int){
        if(percentage<=50){
        lottiecal.visibility = View.VISIBLE
        lottiecal.setAnimation("bronze.json")
            lottiecal.playAnimation()}
        else if(percentage>50 && percentage<=70){
            lottiecal.visibility = View.VISIBLE

            lottiecal.setAnimation("silver.json")
            lottiecal.playAnimation()}
        else if(percentage>70 && percentage<=85){
            lottiecal.visibility = View.VISIBLE
            lottiecal.setAnimation("gold.json")
            lottiecal.playAnimation()}
        else if(percentage>85 && percentage<=100){
            lottiecal.visibility = View.VISIBLE
            lottiecal.setAnimation("diamond.json")
            lottiecal.playAnimation()}
    }

    fun problematic(first:Int,second: Int,third: Int,fourth: Int,fifth: Int) {



        var green:String = "<font color='#4CAF50'>"
        var yellow:String  = "<font color='#FFEB3B'>"
        var red:String  = "<font color='B50000'>"
        var fontend:String  = "</font>"
        var wholehtml:String =""
        var fiz=""
        var sec=""
        var thi=""
        var fort=""
        var fiv =""
        var start = "Problematic: "
        var comma = ","
        var line = "\n"

        if(first<4)
            fiz= "<font color='#B50000'>Fiziološke</font>" +comma
        if(first==4 || first==5)
            fiz= "<font color='#FFEB3B'>Fiziološke</font>" +comma
        if(first==6)
            fiz= "<font color='#4CAF50'>Fiziološke</font>" +comma

        if(second<2)
            sec= "<font color='#B50000'> Sigurnosne</font>" +comma+ line
        if(second==2)
            sec= "<font color='#FFEB3B'> Sigurnosne</font>" +comma + line
        if(second==3)
            sec= "<font color='#4CAF50'> Sigurnosne</font>" +comma + line

        if(third<2)
            thi= "<font color='#B50000'> Pripadanje</font>" +comma
        if(third==2)
            thi= "<font color='#FFEB3B'> Pripadanje</font>" +comma

        if(fourth<1)
            fort= "<font color='#B50000'> Poštovanje</font>" +comma + line
        if(fourth==1)
            fort= "<font color='#FFEB3B'> Poštovanje</font>" +comma + line

        if(fifth<1)
            fiv= "<font color='#B50000'> Samoaktuelizacija</font>"

        if((fiz + sec + thi + fort+ fiv)=="")  problematic.text = start+ " none"
        else
        problematic.text = Html.fromHtml(start +fiz + sec + thi + fort+ fiv)


    }


}
