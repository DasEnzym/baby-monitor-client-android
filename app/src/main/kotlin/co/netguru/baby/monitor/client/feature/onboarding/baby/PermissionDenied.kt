package co.netguru.baby.monitor.client.feature.onboarding.baby

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentDeniedPermissionBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen

class PermissionDenied : BaseFragment() {
    override val layoutResource = R.layout.fragment_denied_permission
    override val screen: Screen = Screen.PERMISSION_DENIED
    private var _binding: FragmentDeniedPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDeniedPermissionBinding.bind(view)
        binding.deniedRetryButtonCtrl.setOnClickListener {
            findNavController().popBackStack(R.id.connectWiFi, false)
        }
        binding.deniedSureButtonCtrl.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
