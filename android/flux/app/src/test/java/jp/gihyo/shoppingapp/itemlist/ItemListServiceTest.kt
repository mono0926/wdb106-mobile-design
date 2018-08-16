package jp.gihyo.shoppingapp.itemlist

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.gihyo.shoppingapp.api.ShoppingApi
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ItemListServiceTest {

    lateinit var target: ItemListService

    var api: ShoppingApi = mock { }
    var repository: ItemListRepository = mock { }

    @Before
    fun before() {
        target = ItemListService(api, repository)
    }

    @Test
    fun `fetchItems should call api`() {
        whenever(api.getItems()).thenReturn(Single.just(listOf()))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.fetchItems()
                .test()
                .assertComplete()
                .assertNoErrors()


        verify(api, times(1)).getItems()
    }

    @Test
    fun `fetchItems should pass items to repository`() {
        whenever(api.getItems()).thenReturn(Single.just(listOf()))
        whenever(repository.setItems(any())).thenReturn(Completable.complete())

        target.fetchItems()
                .test()
                .assertComplete()
                .assertNoErrors()


        verify(repository, times(1)).setItems(listOf())
    }

    @Test
    fun `observeItems should return value from repository`() {
        whenever(repository.observeItems()).thenReturn(Flowable.just(listOf()))

        target.observeItems()
                .test()
                .assertNoErrors()
                .assertValue(listOf())

    }
}