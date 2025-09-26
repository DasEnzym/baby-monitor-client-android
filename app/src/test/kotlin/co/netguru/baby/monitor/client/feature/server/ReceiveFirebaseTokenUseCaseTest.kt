package co.netguru.baby.monitor.client.feature.server

import co.netguru.baby.monitor.client.data.DataRepository
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ReceiveFirebaseTokenUseCaseTest {

    private val dataRepository: DataRepository = mock()
    private val receiveFirebaseTokenUseCase = ReceiveFirebaseTokenUseCase(dataRepository)

    @Test
    fun `should save received token in dataRepository`() {
        val address = "address"
        val token = "token"
        receiveFirebaseTokenUseCase.receiveToken(address, token)

        verify(dataRepository).insertClientData(check {
            assertEquals(address, it.address)
            assertEquals(token, it.firebaseKey)
        })
    }
}
