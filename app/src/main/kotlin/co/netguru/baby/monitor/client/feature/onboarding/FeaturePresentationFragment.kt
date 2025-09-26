package co.netguru.baby.monitor.client.feature.onboarding

import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.databinding.FragmentFeatureABinding
import co.netguru.baby.monitor.client.databinding.FragmentFeatureBBinding
import co.netguru.baby.monitor.client.databinding.FragmentFeatureCBinding
import co.netguru.baby.monitor.client.databinding.OnboardingButtonsBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen
import javax.inject.Inject

class FeaturePresentationFragment : BaseFragment() {

    override var layoutResource = R.layout.fragment_feature_a
    override val screen: Screen = Screen.ONBOARDING
    @Inject
    lateinit var finishOnboardingUseCase: FinishOnboardingUseCase

    private var _binding: ViewBinding? = null
    private val onboardingButtonsBinding: OnboardingButtonsBinding
        get() = when (val binding = _binding) {
            is FragmentFeatureABinding -> binding.onboardingButtons
            is FragmentFeatureBBinding -> binding.onboardingButtons
            is FragmentFeatureCBinding -> binding.onboardingButtons
            else -> throw IllegalStateException("Binding not initialized")
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val featureLayout = when (arguments?.getString(FEATURE_KEY)) {
            FEATURE_B -> FragmentFeatureBBinding.inflate(inflater, container, false).also {
                layoutResource = R.layout.fragment_feature_b
            }
            FEATURE_C -> FragmentFeatureCBinding.inflate(inflater, container, false).also {
                layoutResource = R.layout.fragment_feature_c
            }
            else -> FragmentFeatureABinding.inflate(inflater, container, false).also {
                layoutResource = R.layout.fragment_feature_a
            }
        }
        _binding = featureLayout
        return featureLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (_binding as? FragmentFeatureCBinding)?.tos?.apply {
            text = HtmlCompat.fromHtml(
                getString(R.string.tos_confirmation),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            movementMethod = LinkMovementMethod.getInstance()
        }
        onboardingButtonsBinding.featureNextBtn.setOnClickListener {
            handleNextClicked()
        }
        onboardingButtonsBinding.featureSkipBtn.setOnClickListener {
            findNavController().navigate(finishOnboarding())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleNextClicked() {
        val nextFeature = when (layoutResource) {
            R.layout.fragment_feature_a -> FEATURE_B
            R.layout.fragment_feature_b -> FEATURE_C
            else -> ""
        }
        val bundle = Bundle().apply {
            putString(FEATURE_KEY, nextFeature)
        }
        findNavController().navigate(
            if (nextFeature.isEmpty()) {
                finishOnboarding()
            } else {
                R.id.featureToFeature
            },
            bundle
        )
    }

    private fun finishOnboarding(): Int {
        finishOnboardingUseCase.finishOnboarding()
        return R.id.onboardingToInfoAboutDevices
    }

    companion object {
        private const val FEATURE_KEY = "FEATURE_KEY"
        private const val FEATURE_B = "FEATURE_B"
        private const val FEATURE_C = "FEATURE_C"
    }
}
