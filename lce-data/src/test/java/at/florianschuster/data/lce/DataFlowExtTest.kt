package at.florianschuster.data.lce

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

class DataFlowExtTest {

    @Test
    fun `data flow extension converts correctly`() = runBlockingTest {
        val error = Exception()
        val dataList = (1 until 10).asFlow()
            .map {
                if (it == 5) throw error
                it
            }
            .mapAsData()
            .toList()

        assertEquals(
            listOf(
                Data.Success(1),
                Data.Success(2),
                Data.Success(3),
                Data.Success(4),
                Data.Failure(error)
            ),
            dataList
        )
    }

    @Test
    fun `filterSuccessData only filters success`() = runBlockingTest {
        val dataList = flow {
            emit(Data.Success(Unit))
            emit(Data.Success(Unit))
            emit(Data.Success(Unit))
            emit(Data.Failure(Exception()))
            emit(Data.Success(Unit))
        }.filterSuccessData().toList()

        assertEquals(listOf(Unit, Unit, Unit, Unit), dataList)
    }

    @Test
    fun `filterFailureData only filters failure`() = runBlockingTest {
        val error = Exception()
        val dataList = flow {
            emit(Data.Success(Unit))
            emit(Data.Success(Unit))
            emit(Data.Success(Unit))
            emit(Data.Failure(error))
            emit(Data.Success(Unit))
        }.filterFailureData().toList()

        assertEquals(listOf(error), dataList)
    }

    @Test
    fun `onEach extensions`() = runBlockingTest {
        val valueList = mutableListOf<Int>()
        val error = IOException()
        val errorList = mutableListOf<Throwable>()
        flow {
            emit(Data.Success(1))
            emit(Data.Success(2))
            emit(Data.Failure(error))
        }.onEachDataSuccess { valueList.add(it) }.onEachDataFailure { errorList.add(it) }.collect()

        assertEquals(listOf(1, 2), valueList)
        assertEquals(listOf<Throwable>(error), errorList)
    }
}