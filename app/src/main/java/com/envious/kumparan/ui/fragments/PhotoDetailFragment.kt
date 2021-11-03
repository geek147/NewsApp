package com.envious.kumparan.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.envious.kumparan.databinding.FragmentPhotoDetailBinding
import com.envious.kumparan.util.BindingConverters

class PhotoDetailFragment : Fragment() {

    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_URL = "EXTRA_BODY"

        @JvmStatic
        fun create(
            title: String,
            url: String
        ): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_URL, url)
            }

            return fragment
        }
    }

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private var title: String = ""
    private var url: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotoDetailBinding.inflate(layoutInflater)
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
        url = arguments?.getString(EXTRA_URL).orEmpty()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpImageFull()
    }

    private fun setUpImageFull() {
        with(binding) {
            textTitle.text = title
            BindingConverters.loadImage(imageFull, url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
