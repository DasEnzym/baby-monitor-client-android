package co.netguru.baby.monitor.client.feature.client.home.livecamera

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.PermissionUtils
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.common.extensions.scaleAnimation
import co.netguru.baby.monitor.client.common.extensions.showSnackbarMessage
import co.netguru.baby.monitor.client.databinding.FragmentClientLiveCameraBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen
import co.netguru.baby.monitor.client.feature.babynotification.BabyEventActionIntentService
import co.netguru.baby.monitor.client.feature.client.home.BackButtonState
import co.netguru.baby.monitor.client.feature.client.home.ClientHomeViewModel
import co.netguru.baby.monitor.client.feature.communication.webrtc.ConnectionState
import co.netguru.baby.monitor.client.feature.communication.webrtc.RtcConnectionState
import co.netguru.baby.monitor.client.feature.communication.webrtc.StreamState
import co.netguru.baby.monitor.client.feature.communication.websocket.RxWebSocketClient
import timber.log.Timber
import java.net.URI
import javax.inject.Inject

class ClientLiveCameraFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_client_live_camera
    override val screen: Screen = Screen.CLIENT_LIVE_CAMERA

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), factory)[ClientHomeViewModel::class.java]
    }

    private val fragmentViewModel by lazy {
        ViewModelProviders.of(this, factory)[ClientLiveCameraFragmentViewModel::class.java]
    }
    private var _binding: FragmentClientLiveCameraBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClientLiveCameraBinding.bind(view)
        viewModel.setBackButtonState(
            BackButtonState(
                true,
                shouldShowSnoozeDialogOnBack()
            )
        )
        setupPushToSpeakButton()
        setupObservers()
    }

    private fun setupPushToSpeakButton() {
        if (PermissionUtils.arePermissionsGranted(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            )
        ) {
            enablePushToSpeakButton()
        } else {
            binding.pushToSpeakButton.isVisible = false
        }
    }

    private fun enablePushToSpeakButton() {
        binding.pushToSpeakButton.isVisible = true
        binding.pushToSpeakButton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                onPushToSpeakButtonPressed()
            } else if (event.action == MotionEvent.ACTION_UP) {
                onPushToSpeakButtonRelease()
            }
            true
        }
    }

    private fun onPushToSpeakButtonRelease() {
        fragmentViewModel.pushToSpeak(false)
        binding.pushToSpeakButton.scaleAnimation(
            false,
            PRESSED_SCALE,
            NORMAL_SCALE,
            ANIMATION_DURATION
        )
    }

    private fun onPushToSpeakButtonPressed() {
        fragmentViewModel.pushToSpeak(true)
        binding.pushToSpeakButton.scaleAnimation(
            true,
            PRESSED_SCALE,
            NORMAL_SCALE,
            ANIMATION_DURATION
        )
    }

    private fun setupObservers() {
        viewModel.selectedChildAvailability.observe(
            viewLifecycleOwner,
            Observer { it?.let(::onAvailabilityChange) })
        fragmentViewModel.streamState.observe(viewLifecycleOwner, Observer {
            handleStreamStateChange(it)
        })
    }

    override fun onStart() {
        super.onStart()
        maybeStartCall()
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel.endCall()
    }

    private fun shouldShowSnoozeDialogOnBack() =
        arguments?.getBoolean(BabyEventActionIntentService.SHOULD_SHOW_SNOOZE_DIALOG) == true

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setBackButtonState(
            BackButtonState(
                shouldBeVisible = false,
                shouldShowSnoozeDialog = false
            )
        )
    }

    private fun onAvailabilityChange(connectionAvailable: Boolean) {
        Timber.d("onAvailabilityChange($connectionAvailable)")
        if (connectionAvailable) {
            maybeStartCall()
        } else {
            fragmentViewModel.endCall()
        }
    }

    private fun maybeStartCall() {
        if (!fragmentViewModel.callInProgress.get()) startCall(
            viewModel.rxWebSocketClient,
            PermissionUtils.arePermissionsGranted(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            )
        )
    }

    private fun startCall(rxWebSocketClient: RxWebSocketClient, hasRecordAudioPermission: Boolean) {
        val serverUri = URI.create(viewModel.selectedChildLiveData.value?.address ?: return)
        fragmentViewModel.startCall(
            requireActivity().applicationContext,
            binding.liveCameraRemoteRenderer,
            serverUri,
            rxWebSocketClient,
            hasRecordAudioPermission
        )
    }

    private fun handleStreamStateChange(streamState: StreamState) {
        when ((streamState as? ConnectionState)?.connectionState) {
            RtcConnectionState.Connected,
            RtcConnectionState.Completed -> binding.streamProgressBar.isVisible = false
            RtcConnectionState.Checking -> binding.streamProgressBar.isVisible = true
            RtcConnectionState.Error -> handleBabyDeviceSdpError()
            else -> Unit
        }
    }

    private fun handleBabyDeviceSdpError() {
        showSnackbarMessage(R.string.stream_error)
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pushToSpeakButton.setOnTouchListener(null)
        _binding = null
    }

    companion object {
        private const val NORMAL_SCALE = 1.0f
        private const val PRESSED_SCALE = 2.5f
        private const val ANIMATION_DURATION = 500L
    }
}
