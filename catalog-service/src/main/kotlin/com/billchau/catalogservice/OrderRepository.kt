package com.billchau.catalogservice

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: JpaRepository<Order, Int> {
    fun findByCategory(category: String): List<Order>
}