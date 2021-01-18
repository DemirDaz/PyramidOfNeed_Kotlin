package com.example.tabbar.Fragments

//import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.tabbar.Adapter.PagerViewAdapter
import com.example.tabbar.MainActivity
import com.example.tabbar.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass.
 *//*
class someTask() : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        // ...
        var result = URL(params).readText()
        return result

    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}
*/
class ListFragment : Fragment()   {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    private lateinit var addBtn:com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var printhere:TextView
    private lateinit var ll: LinearLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var visualBtn: ImageButton

    //private var jsonURL = "https://jsonplaceholder.typicode.com/todos/1"
    private var userURL = "https://localhost:44386/api/user"
    private var jsonURL = "http://10.0.2.2:44386/api/user"
    private lateinit var pd: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list, container, false)

    }

    fun promeniPiramidu() {
        var piramida: ImageView? =  activity?.findViewById(R.id.piramida)
        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)

        if(pref.getInt("level",-1)==0)
            piramida?.setImageResource(R.drawable.inactive)
        if(pref.getInt("level",-1)==1)
            piramida?.setImageResource(R.drawable.pyramid1)
        if(pref.getInt("level",-1)==2)
            piramida?.setImageResource(R.drawable.pyramid2)
        if(pref.getInt("level",-1)==3)
            piramida?.setImageResource(R.drawable.pyramid3)
        if(pref.getInt("level",-1)==4)
            piramida?.setImageResource(R.drawable.pyramid4)
        if(pref.getInt("level",-1)==5)
            piramida?.setImageResource(R.drawable.pyramid5)
        if(pref.getInt("level",-1)==6)
            piramida?.setImageResource(R.drawable.filled)


    }



    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        mViewPager = activity?.findViewById(R.id.mViewPager)!!
        visualBtn = activity?.findViewById(R.id.visualBtn)!!


        addBtn = requireView().findViewById(R.id.addBtn)
        printhere = requireView().findViewById(R.id.printhere)
        ll = requireView().findViewById(R.id.linearLayout)

        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)
        val editor : SharedPreferences.Editor
        editor = pref.edit()
       // val current = LocalDateTime.now()
        //val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd ")
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd ")
        val currentDate:String = sdf.format(date) //lokalno vreme
        val trenutno :Date = sdf.parse(currentDate)



        //ll.orientation = LinearLayout.VERTICAL





        //val settings = PreferenceManager.getDefaultSharedPreferences(this.context)
        if(pref.getInt("level",-1)==0){
            printhere.text="Počnite zadatke klikom na +"
            addBtn.isClickable = true

           // pref.getString("now",null)

        }
        if(pref.getInt("level",-1)==1){




            var first = "<font color='#BABABA'>Novi nivo otvoren: </font>"
            var next = "<font color='#A61626' ><i>Fiziološke potrebe</i></font>"
            var third = "\n Odaberite svoje ispunjene potrebe:"
            printhere.text = Html.fromHtml(first + next+ third)
            //printhere.text="Novi nivo otvoren:"+ "Fiziološke potrebe" + "\n" +
                   // "Odaberite svoje ispunjene potrebe:"
            for(i in 1..7){
                if(pref.getString(i.toString(),null)!=null){
                try{
                    val cb =
                        CheckBox(requireActivity().applicationContext)

                    cb.id = i

                    cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )


                    params.bottomMargin = 20
                    params.topMargin = 20

                    cb.textSize = 20F
                    cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                    cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                    cb.text = pref.getString(i.toString(),null)

                    cb.typeface = Typeface.DEFAULT_BOLD
                    cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                    ll.addView(cb,params)
                }
            catch (e:Exception){}
            }
            }
        }
        if(pref.getInt("level",-1)==2){
            var first = "<font color='#BABABA'>Novi nivo otvoren: </font>"
            var next = "<font color='#A61626' ><i>Potrebe sigurnosti</i></font>"
            var third = "\n Odaberite svoje ispunjene potrebe:"
            printhere.text = Html.fromHtml(first + next+ third)
            for(i in 1..19){
                if(pref.getString(i.toString(),null)!=null){
                    try{
                        val cb =
                            CheckBox(requireActivity().applicationContext)

                        cb.id = i

                        cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                        params.bottomMargin = 20
                        params.topMargin = 20

                        cb.textSize = 20F
                        cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                        cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                        cb.text = pref.getString(i.toString(),null)

                        cb.typeface = Typeface.DEFAULT_BOLD
                        cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                        ll.addView(cb,params)
                    }
                    catch (e:Exception){}
                }
            }
        }
        if(pref.getInt("level",-1)==3){
            var first = "<font color='#BABABA'>Novi nivo otvoren: </font>"
            var next = "<font color='#A61626' ><i>Potrebe pripadanja</i></font>"
            var third = "\n Odaberite svoje ispunjene potrebe:"
            printhere.text = Html.fromHtml(first + next+ third)

            for(i in 1..29){
                if(pref.getString(i.toString(),null)!=null){
                    try{
                        val cb =
                            CheckBox(requireActivity().applicationContext)

                        cb.id = i

                        cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                        params.bottomMargin = 20
                        params.topMargin = 20

                        cb.textSize = 20F
                        cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                        cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                        cb.text = pref.getString(i.toString(),null)

                        cb.typeface = Typeface.DEFAULT_BOLD
                        cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                        ll.addView(cb,params)
                    }
                    catch (e:Exception){}
                }
            }
        }
        if(pref.getInt("level",-1)==4){
            var first = "<font color='#BABABA'>Novi nivo otvoren: </font>"
            var next = "<font color='#A61626' ><i>Potrebe poštovanja</i></font>"
            var third = "\n Odaberite svoje ispunjene potrebe:"
            printhere.text = Html.fromHtml(first + next+ third)
            for(i in 1..39){
                if(pref.getString(i.toString(),null)!=null){
                    try{
                        val cb =
                            CheckBox(requireActivity().applicationContext)

                        cb.id = i

                        cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                        params.bottomMargin = 20
                        params.topMargin = 20

                        cb.textSize = 20F
                        cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                        cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                        cb.text = pref.getString(i.toString(),null)

                        cb.typeface = Typeface.DEFAULT_BOLD
                        cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                        ll.addView(cb,params)
                    }
                    catch (e:Exception){}
                }
            }
        }
        if(pref.getInt("level",-1)==5){
            sv.setPadding(10,440,0,0)
            var first = "<font color='#BABABA'>Novi nivo otvoren: </font>"
            var next = "<font color='#A61626' ><i>Samo-aktuelizacija</i></font>"
            var third = "\n „Muzičar mora da stvara muziku, umetnik mora da slika, pesnik mora da piše, ako želi da bude u miru sa sobom. Kakav čovek može biti, takav mora biti “- Abraham Maslov o samoaktualizaciji"
            printhere.text = Html.fromHtml(first + next+ third)



            for(i in 1..45){

                if(pref.getString(i.toString(),null)!=null){
                    try{
                        val cb =
                            CheckBox(requireActivity().applicationContext)

                        cb.id = i

                        cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                        params.bottomMargin = 20
                        params.topMargin = 20

                        cb.textSize = 20F
                        cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                        cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                        cb.text = pref.getString(i.toString(),null)

                        cb.typeface = Typeface.DEFAULT_BOLD
                        cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                        ll.addView(cb,params)
                    }
                    catch (e:Exception){}
                }
            }
        }
        if(pref.getInt("level",-1)==6){
            printhere.text=" Ispunjen dan! Svaka čast!"
            for(i in 1..45){
                if(pref.getString(i.toString(),null)!=null){
                    try{
                        val cb =
                            CheckBox(requireActivity().applicationContext)

                        cb.id = i

                        cb.textAlignment = View.TEXT_ALIGNMENT_INHERIT
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                        params.bottomMargin = 20
                        params.topMargin = 20

                        cb.textSize = 20F
                        cb.buttonTintList = getColorStateList(requireView().context,R.color.trans_pink);
                        cb.setTextColor(getColorStateList(requireView().context,R.color.trans_white))
                        cb.text = pref.getString(i.toString(),null)

                        cb.typeface = Typeface.DEFAULT_BOLD
                        cb.setTypeface(cb.typeface, Typeface.BOLD_ITALIC)


                        ll.addView(cb,params)
                    }
                    catch (e:Exception){}
                }
            }
            var ukupno = pref.getInt("poeni1",-1) + pref.getInt("poeni2",-1) + pref.getInt("poeni3",-1)+ pref.getInt("poeni4",-1) + pref.getInt("poeni5",-1)
            if(ukupno == 17) lottiesucc.visibility = View.VISIBLE
        }



        //printhere.text = currentDate

        addBtn.setOnClickListener {
            /*pd = ProgressDialog(this.context)
            pd.setTitle("Download JSON")
            pd.setMessage("Downloading...Please wait")
            pd.show() */

            //uzmi obelezene, updejtuj poene, mkani iz mogucih
            if (pref.getInt("level", -1) == 0) {

                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)



                //ne upisuje dobar datum
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }
                    uiThread{

                    }
                }
                editor.putInt("level",1)
                editor.apply()
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)



            }
            if (pref.getInt("level", -1) == 1) {
                for (i in 1..7) {
                    var poeni = 0
                    try {
                        var combo: CheckBox = requireView().findViewById(i)
                        if (combo.isChecked) {

                            poeni =pref.getInt("poeni1",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni = poeni + 1
                            editor.putInt("poeni1",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit()
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

                if(pref.getInt("poeni1",-1)>2)
                {   editor.putInt("level",2)
                    editor.commit()
                }

                //VREME SAD
                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)

                //sad je ok
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }
                    uiThread{

                    }
                }
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)

                }
            if (pref.getInt("level", -1) == 2) {

                for (i in 1..19) {
                    var poeni = 0
                    if(pref.getString(i.toString(),null)!=null){
                    try {
                        var combo: CheckBox = requireView().findViewById(i)
                        if (combo.isChecked) {
                            if(i in 1..9){
                            poeni =pref.getInt("poeni1",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni +=1
                            editor.putInt("poeni1",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                            if(i in 10..19){
                                poeni =pref.getInt("poeni2",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni2",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit() }
                        }
                    } catch (e: java.lang.Exception) {
                    }
                } }

                if(pref.getInt("poeni2",-1)>1)
                {editor.putInt("level",3)
                editor.commit()}

                //VREME SAD
                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)

                //sad je ok
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }

                }
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)
            }
            if (pref.getInt("level", -1) == 3) {

            for (i in 1..29) {
            var poeni=0
                if(pref.getString(i.toString(),null)!=null){
                try {
                    var combo: CheckBox = requireView().findViewById(i)
                    if (combo.isChecked) {
                        if(i in 1..9){
                            poeni =pref.getInt("poeni1",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni1",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                        if(i in 10..19){
                            poeni =pref.getInt("poeni2",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni2",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                        if(i in 20..29){
                            poeni =pref.getInt("poeni3",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni3",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                    }
                } catch (e: java.lang.Exception) {
                }
            } }

            if(pref.getInt("poeni3",-1)>1) {
                editor.putInt("level", 4)
                editor.commit()
            }
                //VREME SAD
                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)

                //sad je ok
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }
                }
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)
        }
            if (pref.getInt("level", -1) == 4) {

            for (i in 1..39) {
                var poeni = 0
                if(pref.getString(i.toString(),null)!=null){
                try {
                    var combo: CheckBox = requireView().findViewById(i)
                    if (combo.isChecked) {
                        if(i in 1..9){
                            poeni =pref.getInt("poeni1",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni1",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                        if(i in 10..19){
                            poeni =pref.getInt("poeni2",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni2",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                        if(i in 20..29){
                            poeni =pref.getInt("poeni3",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni3",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                        if(i in 30..39){
                            poeni =pref.getInt("poeni4",-1)
                            //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                            poeni+=1
                            editor.putInt("poeni4",poeni)
                            editor.commit()
                            //makni iz mogucih
                            editor.remove(i.toString())
                            editor.commit() }
                    }
                } catch (e: java.lang.Exception) {
                } }
            }

            if(pref.getInt("poeni4",-1)>0){
                editor.putInt("level",5)
                editor.commit() }
                //VREME SAD
                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)

                //sad je ok
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }

                }
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)
        }
            if (pref.getInt("level", -1) == 5) {


                for (i in 1..49) {
                    var poeni = 0
                    if(pref.getString(i.toString(),null)!=null){
                    try {
                        var combo: CheckBox = requireView().findViewById(i)
                        if (combo.isChecked) {
                            if(i in 1..9){
                                poeni =pref.getInt("poeni1",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni1",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit() }
                            if(i in 10..19){
                                poeni =pref.getInt("poeni2",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni2",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit() }
                            if(i in 20..29){
                                poeni =pref.getInt("poeni3",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni3",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit() }
                            if(i in 30..39){
                                poeni =pref.getInt("poeni4",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni4",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit() }
                            if(i in 40..49){
                                poeni =pref.getInt("poeni5",-1)
                                //povecaj broj pogodjenih, ako je preko pola, poveca nivo na kraj
                                poeni+=1
                                editor.putInt("poeni5",poeni)
                                editor.commit()
                                //makni iz mogucih
                                editor.remove(i.toString())
                                editor.commit()
                                addBtn.isClickable = false
                            }
                        }
                    } catch (e: java.lang.Exception) {
                    } }
                }

                if(pref.getInt("poeni5",-1)>0){
                    editor.putInt("level",6)
                    editor.commit() }
                //VREME SAD
                val sada : LocalDate = LocalDate.parse(pref.getString("now",null)!!)

                //sad je ok
                doAsync {
                    pref.getString("email",null)?.let { it1 -> sendPostRequest(it1,sada,pref.getInt("poeni1",-1),pref.getInt("poeni2",-1),pref.getInt("poeni3",-1),pref.getInt("poeni4",-1),pref.getInt("poeni5",-1)) }

                }
                promeniPiramidu()
                activity?.recreate()
                mViewPager.currentItem=1
                visualBtn.setImageResource(R.drawable.ic_star_half_pink)
            }


            promeniPiramidu()
            activity?.recreate()


        /*
            if (pref.getInt("level", -1) == 1) {
                var m = 0
                for (i in 1..7) {
                    if(pref.getString(i.toString(),null)==null)
                        m++}
                if (m > 3) {
                    editor.putInt("level", 2)
                    editor.apply()
            } */





            //fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }
    }

    fun sendPostRequest( user:String, date: LocalDate, c1:Int,c2:Int,c3:Int,c4:Int,c5:Int ) {

    //var reqParam = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id.toString(), "UTF-8")
     var reqParam =URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8")
        reqParam += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date.toString(), "UTF-8")
        reqParam += "&" + URLEncoder.encode("C1", "UTF-8") + "=" + URLEncoder.encode(c1.toString(), "UTF-8")
        reqParam += "&" + URLEncoder.encode("C2", "UTF-8") + "=" + URLEncoder.encode(c2.toString(), "UTF-8")
        reqParam += "&" + URLEncoder.encode("C3", "UTF-8") + "=" + URLEncoder.encode(c3.toString(), "UTF-8")
        reqParam += "&" + URLEncoder.encode("C4", "UTF-8") + "=" + URLEncoder.encode(c4.toString(), "UTF-8")
        reqParam += "&" + URLEncoder.encode("C5", "UTF-8") + "=" + URLEncoder.encode(c5.toString(), "UTF-8")
    val mURL = URL("http://10.0.2.2:44386/api/calendar")

    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "POST"

        val wr = OutputStreamWriter(outputStream);
        wr.write(reqParam);
        wr.flush();

        println("URL : $url")
        println("Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            println("Response : $response")
        }
    }
}

            /*
            //Konektuj se sa netom
            doAsync {
                var result = URL(jsonURL).readText()

                val jsonArray = JSONArray(result)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                val date : Any= jsonObject.get("email")
                uiThread {
                    printhere.text = date as CharSequence?
                    pd.dismiss()

                    //pd.dismiss()
                }
            } */






       //{ JSONDownloader(this.context, jsonURL,printhere ).execute() })

        // Inflate the layout for this fragm





            // printhere.setText(result);

        // or  (ImageView) view.findViewById(R.id.foo);






