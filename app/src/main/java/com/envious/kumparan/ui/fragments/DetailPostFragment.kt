package com.envious.kumparan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.envious.kumparan.R
import com.envious.kumparan.base.BaseFragment
import com.envious.kumparan.databinding.DetailPostFragmentBinding
import com.envious.kumparan.ui.adapter.CommentAdapter
import com.envious.kumparan.util.Intent
import com.envious.kumparan.util.State
import com.envious.kumparan.util.ViewState

class DetailPostFragment : BaseFragment<Intent,
    State>() {

    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_BODY = "EXTRA_BODY"
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"

        @JvmStatic
        fun create(
            postId: Int,
            userName: String,
            title: String,
            body: String
        ): DetailPostFragment {
            val fragment = DetailPostFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_POST_ID, postId)
                putString(EXTRA_TITLE, title)
                putString(EXTRA_BODY, body)
                putString(EXTRA_USER_NAME, userName)
            }

            return fragment
        }
    }

    private var _binding: DetailPostFragmentBinding? = null
    private val binding get() = _binding!!

    private var postId: Int = 0
    private var userName: String = ""
    private var title: String = ""
    private var body: String = ""
    private lateinit var adapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DetailPostFragmentBinding.inflate(layoutInflater)
        postId = arguments?.getInt(EXTRA_POST_ID) ?: 0
        userName = arguments?.getString(EXTRA_USER_NAME).orEmpty()
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
        body = arguments?.getString(EXTRA_BODY).orEmpty()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }
        setUpDetailPost()
        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetCommentByPostId(postId))
    }

    private fun setUpDetailPost() {
        with(binding) {
            textUserName.text = userName
            textTitle.text = title
            textBody.text = body
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle the up button here
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) ||
            super.onOptionsItemSelected(item)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override val layoutResourceId: Int
        get() = R.layout.detail_post_fragment

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = CommentAdapter(this@DetailPostFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    override fun invalidate(state: State) {

        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListPhoto -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetCommentByPostId(postId))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorListComment -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetCommentByPostId(postId))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.Idle -> {}
            ViewState.SuccessListComment -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listComment)
                    errorView.visibility = View.GONE
                }
            }
        }
    }
}
