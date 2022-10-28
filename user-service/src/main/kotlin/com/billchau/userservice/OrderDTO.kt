package com.billchau.userservice

data class OrderDTO(
    val id: Int,
    val name: String,
    val category: String,
    val color: String,
    val price: Double
)
