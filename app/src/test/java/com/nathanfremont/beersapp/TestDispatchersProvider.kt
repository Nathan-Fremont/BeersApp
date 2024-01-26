package com.nathanfremont.beersapp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchersProvider : IDispatchersProvider {
    override val io: CoroutineDispatcher
        get() = StandardTestDispatcher()
    override val computation: CoroutineDispatcher
        get() = StandardTestDispatcher()
    override val ui: CoroutineDispatcher
        get() = StandardTestDispatcher()
}