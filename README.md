# circuit-breaker-kotlin
A demo application of circuit-breaker written in Kotlin

This is a practice project of circuit breaker

After searching through the internet, the annotation fallback method in resilience4j fail to work as the one in Java due to the mechanism of Kotlin(please correct me if I am wrong)

So we have to implement another mechanism to catch the exception and trigger the circuit breaker.


Reference

https://www.youtube.com/watch?v=b6R4dElDtRc

https://github.com/volkanozkan/resilience4j-kotlin-demo
