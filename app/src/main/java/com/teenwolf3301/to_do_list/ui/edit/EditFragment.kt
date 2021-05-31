package com.teenwolf3301.to_do_list.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.databinding.FragmentEditBinding

class EditFragment : Fragment(R.layout.fragment_edit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditBinding.bind(view)
    }
}