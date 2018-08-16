package jp.gihyo.shoppingapp.cart

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.model.CartItem
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CartRepositoryTest {

    private lateinit var target: CartRepository

    private val behaviorProcessor: BehaviorProcessor<List<CartItem>> = mock { }

    @Before
    fun setUp() {
        target = CartRepository(behaviorProcessor)
    }

    @Test
    fun `observeItems should return items from behaviorProcessor`() {
        whenever(behaviorProcessor.hide()).thenReturn(Flowable.just(listOf()))

        target.observeItems()
                .test()
                .assertNoErrors()
                .assertValue(listOf())

        verify(behaviorProcessor, times(1)).hide()
    }

    @Test
    fun `setItems should set items to behavior processor`() {

        target.setItems(listOf())
                .test()
                .assertNoErrors()
                .assertComplete()

        verify(behaviorProcessor, times(1)).onNext(listOf())
    }
}