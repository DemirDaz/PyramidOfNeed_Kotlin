package com.example.tabbar.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tabbar.R
import kotlinx.android.synthetic.main.fragment_introduction.*

/**
 * A simple [Fragment] subclass.
 */
class introduction : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introduction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottie.setAnimation(arguments?.getString("imageResId"))
        basic_text.text = arguments?.getString("text")
    }

    companion object {
        fun  newInstance(context: Context, imageResId :String, text :String) : Fragment {
            val fragmentFirst = Fragment.instantiate(context,introduction::class.java.name)
            val args = Bundle()
            args.putString("text", text)
            args.putString("imageResId", imageResId)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }

}
