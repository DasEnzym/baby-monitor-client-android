package co.netguru.baby.monitor.client.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentVoiceRecordingsSettingBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen
import co.netguru.baby.monitor.client.feature.settings.ConfigurationViewModel
import javax.inject.Inject

class VoiceRecordingsSettingsFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_voice_recordings_setting
    override val screen: Screen = Screen.VOICE_RECORDINGS_SETTING

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, factory)[ConfigurationViewModel::class.java]
    }
    private var _binding: FragmentVoiceRecordingsSettingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVoiceRecordingsSettingBinding.bind(view)
        binding.featureDNextBtn.setOnClickListener {
            findNavController().navigate(R.id.featureDToConnecting)
        }
        binding.featureDSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setUploadEnabled(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
