package jp.gihyo.shoppingapp.itemlist

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.model.Item
import org.junit.Test

import org.junit.Before

class ItemListRepositoryTest {

    lateinit var target: ItemListRepository


    var behaviorProcessor: BehaviorProcessor<List<Item>> = mock { }

    @Before
    fun before() {
        target = ItemListRepository(behaviorProcessor)
    }

    @Test
    fun `observeItems should return value from behaviorProcessor`() {
        whenever(behaviorProcessor.hide()).thenReturn(
                Flowable.just(
                        listOf()
                )
        )

        target.observeItems()
                .test()
                .assertNoErrors()
                .assertValue(listOf())

    }

    @Test
    fun `setItems should pass items to behaviorProcessor`() {
        val items: List<Item> = listOf()

        target.setItems(items)
                .test()
                .assertComplete()
                .assertNoErrors()

        verify(behaviorProcessor, times(1)).onNext(items)
    }
}