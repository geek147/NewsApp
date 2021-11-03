package com.envious.kumparan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.envious.kumparan.R
import com.envious.kumparan.base.BaseFragment
import com.envious.kumparan.databinding.FragmentUserDetailBinding
import com.envious.kumparan.ui.adapter.AlbumAdapter
import com.envious.kumparan.util.Intent
import com.envious.kumparan.util.State
import com.envious.kumparan.util.ViewState

class UserDetailFragment : BaseFragment<Intent,
    State>() {
    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_USER_ID = "EXTRA_USER_ID"

        @JvmStatic
        fun create(
            userId: Int,
        ): UserDetailFragment {
            val fragment = UserDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_USER_ID, userId)
            }

            return fragment
        }
    }

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private var userId: Int = 0
    private lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailBinding.inflate(layoutInflater)
        userId = arguments?.getInt(DetailPostFragment.EXTRA_POST_ID) ?: 0
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }
        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetDetailUser(userId = userId))
        viewModel.onIntentReceived(Intent.GetAlbumByUserId(userId = userId))
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = AlbumAdapter(this@UserDetailFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_user_detail

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListAlbum -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetAlbumByUserId(userId))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorListAlbum -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetAlbumByUserId(userId))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.Idle -> {}
            ViewState.SuccessListAlbum -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listAlbum)
                    errorView.visibility = View.GONE
                }
            }
            ViewState.SuccessGetDetailUser -> {
                with(binding) {
                    textAddress.text = state.detailUser?.address
                    textUserName.text = state.detailUser?.name
                    textUserCompany.text = state.detailUser?.company
                    textEmail.text = state.detailUser?.email
                }
            }
        }
    }
}
