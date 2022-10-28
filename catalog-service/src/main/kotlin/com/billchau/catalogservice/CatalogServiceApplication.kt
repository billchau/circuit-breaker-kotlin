package com.billchau.catalogservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.annotation.PostConstruct


@SpringBootApplication
@RestController
@RequestMapping("/orders")
class CatalogServiceApplication(val orderRepository: OrderRepository) {
    @PostConstruct
    fun initOrdersTable() {
        orderRepository.saveAll(
            Stream.of(
                Order(null, "mobile", "electronics", "white", 20000.0),
                Order(null, "T-Shirt", "clothes", "black", 999.0),
                Order(null, "Jeans", "clothes", "blue", 1999.0),
                Order(null, "Laptop", "electronics", "gray", 50000.0),
                Order(null, "digital watch", "electronics", "black", 2500.0),
                Order(null, "Fan", "electronics", "black", 50000.0)
            ).collect(Collectors.toList())
        )
    }

    @GetMapping
    fun getOrders(): List<Order?>? {
        return orderRepository.findAll()
    }

    @GetMapping("/{category}")
    fun getOrdersByCategory(@PathVariable category: String?): List<Order?>? {
        return orderRepository.findByCategory(category!!)
    }
}

fun main(args: Array<String>) {
    runApplication<CatalogServiceApplication>(*args)
}
