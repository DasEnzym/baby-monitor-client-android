package co.netguru.baby.monitor.client.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentSpecifyDeviceBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class SpecifyDeviceFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_specify_device
    override val screen: Screen = Screen.SPECIFY_DEVICE
    private var _binding: FragmentSpecifyDeviceBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSpecifyDeviceBinding.bind(view)
        binding.babyCtl.setOnClickListener {
            findNavController().navigate(R.id.specifyDeviceToFeatureD)
        }
        binding.parentCtl.setOnClickListener {
            findNavController().navigate(R.id.specifyDeviceToParentDeviceInfo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
