package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hamza.newsapp.utils.BaseFragment
import com.hamza.newsapp.R
import com.hamza.newsapp.databinding.FragmentSearchNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : BaseFragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchNewsBinding.bind(view)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}