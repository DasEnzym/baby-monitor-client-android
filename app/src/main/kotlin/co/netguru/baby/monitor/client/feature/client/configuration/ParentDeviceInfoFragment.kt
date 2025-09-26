package co.netguru.baby.monitor.client.feature.client.configuration

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentParentDeviceInfoBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class ParentDeviceInfoFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_parent_device_info
    override val screen: Screen = Screen.PARENT_DEVICE_INFO
    private var _binding: FragmentParentDeviceInfoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentParentDeviceInfoBinding.bind(view)
        binding.secondAppButtonCtrl.setOnClickListener {
            findNavController().navigate(R.id.secondAppInfoToServiceDiscovery)
        }

        binding.secondAppInfoBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
