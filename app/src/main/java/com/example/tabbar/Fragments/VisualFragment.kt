package com.example.tabbar.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ViewStubCompat

import com.example.tabbar.R
import kotlinx.android.synthetic.main.fragment_visual.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.find

/**
 * A simple [Fragment] subclass.
 */
class VisualFragment : Fragment() {

    private lateinit var piramida: ImageView
    private lateinit var tekstic: TextView
    //private lateinit var naslov: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)
       // tekstic = getView()!!.findViewWithTag("tutum")
        //tekstic.text = pref.getInt("level",-1).toString()
       // ovde.text = pref.getString("now",null)
        piramida = requireView().findViewById(R.id.piramida)


        if(pref.getInt("level",-1)==0)
            piramida.setImageResource(R.drawable.inactive)
        if(pref.getInt("level",-1)==1)
            piramida.setImageResource(R.drawable.pyramid1)
        if(pref.getInt("level",-1)==2)
            piramida.setImageResource(R.drawable.pyramid2)
        if(pref.getInt("level",-1)==3)
            piramida.setImageResource(R.drawable.pyramid3)
        if(pref.getInt("level",-1)==4)
            piramida.setImageResource(R.drawable.pyramid4)
        if(pref.getInt("level",-1)==5)
            piramida.setImageResource(R.drawable.pyramid5)
        if(pref.getInt("level",-1)==6)
            piramida.setImageResource(R.drawable.filled)

       // naslov = getView()!!.findViewById(R.id.naslov)
        //piramida.tag = R.drawable.inactive
        //var bckName = piramida.getTag().toString();
        piramida.visibility = View.VISIBLE
        // if (bckName == "inactive")
           //  naslov.text = "Maslovljeva Piramida"
        val animationZoomIn = AnimationUtils.loadAnimation(this.context, R.anim.slide_down)
        piramida.startAnimation(animationZoomIn)

        /*ViewStubCompat.OnInflateListener(piramida.){

        } */

    }

}
