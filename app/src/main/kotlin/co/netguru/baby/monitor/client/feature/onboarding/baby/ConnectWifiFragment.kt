package co.netguru.baby.monitor.client.feature.onboarding.baby

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.common.extensions.allPermissionsGranted
import co.netguru.baby.monitor.client.databinding.FragmentConnectWifiBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class ConnectWifiFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_connect_wifi
    override val screen: Screen = Screen.CONNECT_WIFI

    private val wifiReceiver by lazy { WifiReceiver() }
    private var _binding: FragmentConnectWifiBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConnectWifiBinding.bind(view)
        binding.wifiConnectionButton.setOnClickListener {
            if (wifiReceiver.isWifiConnected.value?.fetchData() == true) {
                findNavController().navigate(
                    when {
                        requireContext().allPermissionsGranted(allPermissions)
                        -> R.id.connectWiFiToSetupInformation
                        requireContext().allPermissionsGranted(cameraPermission)
                        -> R.id.connectWiFiToPermissionMicrophone
                        else -> R.id.connectWiFiToPermissionCamera
                    }
                )
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }
        wifiReceiver.isWifiConnected.observe(viewLifecycleOwner, Observer { isConnected ->
            binding.wifiConnectionButton.text = if (isConnected?.fetchData() == true) {
                getString(R.string.connect_wifi_connected)
            } else {
                getString(R.string.connect_to_wi_fi)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(wifiReceiver, WifiReceiver.intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(wifiReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val allPermissions = arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
        )
        private val cameraPermission = arrayOf(
                Manifest.permission.CAMERA
        )
    }
}
