package com.fakhri.products.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.network.api.ProductService

const val page = 10
class ProductsPagingSource(
    private val apiService: ProductService
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
                anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 0
            val response = apiService.getProducts(
                skip = currentPage,
                limit = params.loadSize,
                select = "title,price,tags,images"
            )

            if (response.code() != 200) {
                LoadResult.Error(
                    Exception("Error loading products: ${response.code()} ${response.message()}")
                )
            } else {
                LoadResult.Page(
                    data = response.body()!!.products,
                    prevKey = if (currentPage == 0) null else currentPage - params.loadSize,
                    nextKey = if (response.body()!!.products.isEmpty()) null else currentPage + params.loadSize
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}