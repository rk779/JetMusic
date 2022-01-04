package cf.rk585.vivace.core.base.util

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val network: CoroutineDispatcher
)
