package com.madhavth.firebaselearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_horizantal_list.view.*

class HorizantalListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horizantal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        val myList= bundle?.getStringArrayList("myItems")
        view.itemsList.text = myList.toString()
    }

    companion object
    {
        fun getInstance():HorizantalListFragment
        {
            return HorizantalListFragment()
        }

    }
}
