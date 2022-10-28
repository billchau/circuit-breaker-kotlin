package com.billchau.userservice

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.retry.annotation.Retry
import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.ResiliencyHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.net.ConnectException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.ArrayList
import kotlin.random.Random


@SpringBootApplication
@RestController
@RequestMapping("/user-service")
class UserServiceApplication() {
	companion object{
		const val USER_SERVICE = "userService"
		val BASEURL = "http://localhost:9191/orders"
		var attempt = 1
		val restTemplate = RestTemplate()
		val resiliencyHelper = ResiliencyHelper()
	}

	@GetMapping("/displayOrders") // @CircuitBreaker(name =USER_SERVICE,fallbackMethod = "getAllAvailableProducts")
//	@CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableProducts")
//	@Retry(name = USER_SERVICE,fallbackMethod = "getAllAvailableProducts")
	fun displayOrders(@RequestParam("category") category: String?): List<OrderDTO> {
		val url: String = if (category == null) BASEURL else "$BASEURL/$category"
		println("retry method called " + attempt++ + " times " + " at " + Date())

		return resiliencyHelper.runResiliently(
			name = USER_SERVICE,
			circuitBreakerConfiguration = CircuitBreakerConfiguration()
		) {
			restTemplate.getForObject(url, Array<OrderDTO>::class.java)?.toList()!!
		}
	}


//	fun getAllAvailableProducts(e: Exception?): List<OrderDTO>? {
//		return Stream.of(
//			OrderDTO(119, "LED TV", "electronics", "white", 45000.0),
//			OrderDTO(345, "Headset", "electronics", "black", 7000.0),
//			OrderDTO(475, "Sound bar", "electronics", "black", 13000.0),
//			OrderDTO(574, "Puma Shoes", "foot wear", "black & white", 4600.0),
//			OrderDTO(678, "Vegetable chopper", "kitchen", "blue", 999.0),
//			OrderDTO(532, "Oven Gloves", "kitchen", "gray", 745.0)
//		).collect(Collectors.toList())
//	}

	@ExceptionHandler(ConnectException::class)
	fun exceptionHandler(exception: ConnectException): ResponseEntity<String> {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Connection fail")
	}


	@ExceptionHandler(CallNotPermittedException::class)
	fun exceptionHandler(exception: CallNotPermittedException): ResponseEntity<String> {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Circuit Breaker is not allowing more calls.")
	}

	@ExceptionHandler(RequestNotPermitted::class)
	fun exceptionHandler(exception: RequestNotPermitted): ResponseEntity<String> {
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Reached the rate limit for this api.")
	}

	@ExceptionHandler(IllegalArgumentException::class)
	fun exceptionHandler(exception: IllegalArgumentException): ResponseEntity<String> {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body("Received Illegal Argument Exception. Circuit breaker is still closed and allows for more call.")
	}
}
fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
