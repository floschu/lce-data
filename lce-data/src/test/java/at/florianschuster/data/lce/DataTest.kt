package at.florianschuster.data.lce

import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DataTest {

    @Test
    fun `success data from factory`() {
        val value = 123
        val data = Data { value }

        assertEquals(Data.Success(value), data)
    }

    @Test
    fun `failure data from factory`() {
        val exception = IOException()
        val data = Data { throw exception }

        assertEquals(Data.Failure(exception), data)
    }

    @Test
    fun `invoke only contains element at success`() {
        assertNull(Data.Uninitialized.invoke())
        assertNull(Data.Loading.invoke())
        assertNull(Data.Failure(Exception()).invoke())
        assertNotNull(Data.Success(1).invoke())
    }

    @Test
    fun `data equals`() {
        var data: Data<Int> = Data.Uninitialized
        assertEquals(Data.Uninitialized, data)

        data = Data.Loading
        assertEquals(Data.Loading, data)

        val error = IOException()
        data = Data.Failure(error)
        assertEquals(Data.Failure(error), data)

        val value = 42
        data = Data.Success(value)
        assertEquals(Data.Success(value), data)

        println(data)
    }
}