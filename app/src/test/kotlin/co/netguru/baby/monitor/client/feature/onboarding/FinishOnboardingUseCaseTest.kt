package co.netguru.baby.monitor.client.feature.onboarding

import co.netguru.baby.monitor.client.data.DataRepository
import co.netguru.baby.monitor.client.data.splash.AppState
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Test

class FinishOnboardingUseCaseTest {

    private val dataRepository = mock<DataRepository>()
    private val finishOnboardingUseCase = FinishOnboardingUseCase(dataRepository)

    @Test
    fun `should save undefined state`() {
        finishOnboardingUseCase.finishOnboarding()

        verify(dataRepository).saveConfiguration(AppState.UNDEFINED)
    }
}
