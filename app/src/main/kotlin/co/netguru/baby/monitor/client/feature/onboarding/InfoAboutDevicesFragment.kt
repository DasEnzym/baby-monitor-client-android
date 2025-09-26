package co.netguru.baby.monitor.client.feature.onboarding

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentInfoAboutDevicesBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class InfoAboutDevicesFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_info_about_devices
    override val screen: Screen = Screen.INFO_ABOUT_DEVICES
    private var _binding: FragmentInfoAboutDevicesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInfoAboutDevicesBinding.bind(view)
        binding.specifyDeviceDescriptionTv.text = Html.fromHtml(getString(R.string.sync_description))
        binding.specifyDeviceBtn.setOnClickListener {
            findNavController().navigate(R.id.infoAboutDevicesToSpecifyDevice)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
