package com.envious.kumparan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.envious.kumparan.R
import com.envious.kumparan.base.BaseFragment
import com.envious.kumparan.databinding.PostFragmentBinding
import com.envious.kumparan.ui.adapter.PostAdapter
import com.envious.kumparan.util.Intent
import com.envious.kumparan.util.State
import com.envious.kumparan.util.ViewState

class PostFragment :
    BaseFragment<Intent,
        State>() {

    companion object {
        fun newInstance() = PostFragment()
    }

    private var _binding: PostFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PostFragmentBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }
        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetPost)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = PostAdapter(this@PostFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    override val layoutResourceId: Int
        get() = R.layout.post_fragment

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListPost -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetPost)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorListPost -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetPost)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.Idle -> {}
            ViewState.SuccessListPost -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listPost)
                    errorView.visibility = View.GONE
                }
            }
        }
    }
}
