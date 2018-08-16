package jp.gihyo.shoppingapp.itemlist

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import jp.gihyo.shoppingapp.cart.CartService
import jp.gihyo.shoppingapp.model.Item
import org.junit.Before
import org.junit.Test

class ItemListViewModelTest {

    private lateinit var target: ItemListViewModel

    private var service: ItemListService = mock { }
    private var cartService: CartService = mock { }

    @Before
    fun setUp() {
        target = ItemListViewModel(service, cartService)
    }

    @Test
    fun `fetchItems should call ItemListService`() {
        whenever(service.fetchItems()).thenReturn(Completable.complete())

        target.fetchItems()
                .test()
                .assertComplete()
                .assertNoErrors()

        verify(service, times(1)).fetchItems()
    }

    @Test
    fun `observeItems should return items from ItemListService`() {
        whenever(service.observeItems()).thenReturn(Flowable.just(listOf()))

        target.observeItems()
                .test()
                .assertNoErrors()
                .assertValue(listOf())

        verify(service, times(1)).observeItems()
    }

    @Test
    fun `addItemToCart should call CartService`() {
        whenever(cartService.addItem(any())).thenReturn(Completable.complete())
        val item: Item = mock { }


        target.addItemToCart(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        verify(cartService, times(1)).addItem(item)
    }

    @Test
    fun `removeItemFromCart should call CartService`() {
        whenever(cartService.removeItem(any())).thenReturn(Completable.complete())
        val item: Item = mock { }


        target.removeItemFromCart(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        verify(cartService, times(1)).removeItem(item)
    }
}