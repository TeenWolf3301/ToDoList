package com.teenwolf3301.to_do_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.databinding.FragmentListBinding
import java.text.DateFormat

class ListFragment : Fragment() {

    private lateinit var listRecyclerView: RecyclerView

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)

        listRecyclerView = binding.rvList
        binding.tvDate.text = DateFormat.getDateInstance().format(System.currentTimeMillis())
        binding.tvStats.text = "5 incomplete, 5 completed"

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}