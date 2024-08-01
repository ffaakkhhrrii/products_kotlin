package com.fakhri.products.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.Result

/*
*
* RequestType: Data type that used to catch network response a.k.a inserted data type
* ResultType: Data type that expected as return data a.k.a output data type
* */
abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result: Flow<Resource<ResultType>> = flow{
        emit(Resource.Loading())
        try {
            val apiResponse = requestFromRemote()
            if(apiResponse.isSuccess){
                apiResponse.getOrNull()?.let { res ->
                    onFetchSuccess(res)
                    emitAll(
                        loadResult(res).map {
                            Resource.Success(it)
                        }
                    )
                }

            }else{
                onFetchFailed()
                emit(
                    Resource.Error(
                        message = apiResponse
                            .exceptionOrNull()
                            ?.localizedMessage ?: "Unknown error"
                    )
                )
            }
        }catch (e: Exception){
            // TODO: Add log to Firebase Crashlytic
            onFetchFailed()
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "Unknown error"
                )
            )
        }
    }

    protected abstract suspend fun requestFromRemote(): Result<RequestType>

    /**
     * Load from network to be returned and consumed. Convert data from network to model here
     *
     * @param data
     * @return
     */
    protected abstract fun loadResult(data: RequestType): Flow<ResultType>

    protected open fun onFetchSuccess(data: RequestType) {}

    protected open fun onFetchFailed() {}
    fun asFlow(): Flow<Resource<ResultType>> = result

}