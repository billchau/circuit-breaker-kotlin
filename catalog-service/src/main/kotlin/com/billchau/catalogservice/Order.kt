package com.billchau.catalogservice

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "ORDERS_TBL")
data class Order(
    @Id
    @GeneratedValue
    val id: Int?,
    val name: String,
    val category: String,
    val color: String,
    val price: Double
)
