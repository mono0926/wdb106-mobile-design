package jp.gihyo.shoppingapp.cart

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.model.CartItem
import jp.gihyo.shoppingapp.model.Item
import org.junit.Before
import org.junit.Test

class CartServiceTest {

    private lateinit var target: CartService

    private var api: ShoppingApi = mock { }
    private var repository: CartRepository = mock { }

    @Before
    fun before() {
        target = CartService(api, repository)
    }

    @Test
    fun `observeItems should call CartRepository`() {
        whenever(repository.observeItems()).thenReturn(Flowable.just(listOf()))

        target.observeItems()
                .test()
                .assertNoErrors()
                .assertValue(listOf())

        verify(repository, times(1)).observeItems()
    }

    @Test
    fun `addItem should create a new cart item if list is empty`() {
        whenever(repository.observeItems()).thenReturn(Flowable.just(listOf()))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        val item: Item = mock {  }
        val expected = CartItem(item, 1)

        target.addItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.size == 1)

        assert(result.first() == expected)
    }

    @Test
    fun `addItem should increment the cart item if item in list`() {
        val item: Item = mock { whenever(it.id).thenReturn(1) }
        val cartItem = CartItem(item, 1)
        val expected = CartItem(item, 2)

        whenever(repository.getItems()).thenReturn(listOf(cartItem))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.addItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.size == 1)

        assert(result.first() == expected)

    }

    @Test
    fun `removeItem should remove the CartItem if count is 1`() {
        val item: Item = mock { whenever(it.id).thenReturn(1) }
        val cartItem = CartItem(item, 1)

        whenever(repository.getItems()).thenReturn(listOf(cartItem))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.removeItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.isEmpty())
    }

    @Test
    fun `removeItem should decrement the count if count greater than 1`() {
        val item: Item = mock { whenever(it.id).thenReturn(1) }
        val cartItem = CartItem(item, 2)
        val expected = CartItem(item, 1)

        whenever(repository.getItems()).thenReturn(listOf(cartItem))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.removeItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.size == 1)

        assert(result.first() == expected)
    }

    @Test
    fun `removeItem should do nothing if item not found in list`() {
        val item: Item = mock { whenever(it.id).thenReturn(1) }
        val listItem: Item = mock { whenever(it.id).thenReturn(2) }
        val cartItem = CartItem(listItem, 2)

        whenever(repository.getItems()).thenReturn(listOf(cartItem))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.removeItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.size == 1)

        assert(result.first() == cartItem)
    }

    @Test
    fun `removeItem should do nothing if list is empty`() {
        val item: Item = mock { whenever(it.id).thenReturn(1) }

        whenever(repository.getItems()).thenReturn(listOf())
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.removeItem(item)
                .test()
                .assertComplete()
                .assertNoErrors()

        val argumentCaptor = argumentCaptor<List<CartItem>>()

        verify(repository, times(1)).setItems(argumentCaptor.capture())

        val result = argumentCaptor.firstValue

        assert(result.isEmpty())
    }

}