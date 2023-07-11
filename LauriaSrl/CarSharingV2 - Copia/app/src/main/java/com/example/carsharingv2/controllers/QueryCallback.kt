package com.example.carsharingv2.controllers

import org.jetbrains.annotations.NotNull

interface QueryCallback
{
    fun success(@NotNull response: Any, success: String)

    fun failure(@NotNull fail: String)
}