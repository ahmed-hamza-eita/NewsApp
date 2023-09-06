package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hamza.newsapp.utils.BaseFragment
import com.hamza.newsapp.R
import com.hamza.newsapp.adapters.NewsAdapter
import com.hamza.newsapp.databinding.FragmentSavedNewsBinding
import com.hamza.newsapp.utils.Const
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SavedNewsFragment : BaseFragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()

    private val adapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_saved_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedNewsBinding.bind(view)

        actions()
    }

    private fun actions() {
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Const.SERIALIZABLE_KEY, it)

            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}