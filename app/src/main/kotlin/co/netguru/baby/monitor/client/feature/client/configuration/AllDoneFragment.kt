package co.netguru.baby.monitor.client.feature.client.configuration

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentAllDoneBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class AllDoneFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_all_done
    override val screen: Screen = Screen.ALL_DONE
    private var _binding: FragmentAllDoneBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllDoneBinding.bind(view)
        binding.addDoneCtrl.setOnClickListener {
            findNavController().navigate(R.id.allDoneToClientHome)
            requireActivity().finish()
        }
        binding.allDoneBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
