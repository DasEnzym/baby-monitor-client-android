package co.netguru.baby.monitor.client.feature.babycrynotification

import co.netguru.baby.monitor.RxSchedulersOverrideRule
import co.netguru.baby.monitor.client.feature.babynotification.NotifyBabyEventUseCase
import co.netguru.baby.monitor.client.feature.firebasenotification.FirebaseNotificationSender
import co.netguru.baby.monitor.client.feature.firebasenotification.NotificationType
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Rule
import org.junit.Test

class NotifyBabyEventUseCaseTest {

    @get:Rule
    val schedulersRule = RxSchedulersOverrideRule()

    private val notificationSender: FirebaseNotificationSender = mock()
    private val text = "text"
    private val notificationTexts = mock<Map<String, String>> {
        on { get(any<String>()) }.doReturn(text)
    }

    private val notifyBabyEventUseCase =
        NotifyBabyEventUseCase(
            notificationSender,
            notificationTexts
        )

    @Test
    fun `should send crying notification on notifyBabyCrying`() {
        notifyBabyEventUseCase
            .babyEvents()
            .subscribe()

        notifyBabyEventUseCase.notifyBabyCrying()

        verify(notificationSender).broadcastNotificationToFcm(
            text,
            text,
            NotificationType.CRY_NOTIFICATION
        )
    }

    @Test
    fun `should send crying notification on notifyNoiseDetected`() {
        notifyBabyEventUseCase
            .babyEvents()
            .subscribe()

        notifyBabyEventUseCase.notifyNoiseDetected()

        verify(notificationSender).broadcastNotificationToFcm(
            text,
            text,
            NotificationType.NOISE_NOTIFICATION
        )
    }
}
