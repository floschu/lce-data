package at.florianschuster.data.lce

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataFlowBuilderTest {

    @Test
    fun `dataOf builder`() = runBlockingTest {
        val dataSuccess = dataOf { loadSomething() }
        val dataFailure = dataOf { loadSomething(withException = true) }

        advanceTimeBy(1000)

        assertTrue { dataSuccess.successful }
        assertTrue { dataFailure.failed }
    }

    @Test
    fun `dataFlowOf builder with loading delay`() = runBlockingTest {
        val dataList = dataFlowOf { loadSomething() }.toList()

        advanceTimeBy(1000)

        assertEquals(listOf(Data.Loading, Data.Success(something)), dataList)
    }

    private suspend fun loadSomething(
        withException: Boolean = false
    ): String = coroutineScope {
        delay(1000)
        if (withException) throw exception
        something
    }

    companion object {
        private val something = "Something important"
        private val exception = IOException()
    }
}