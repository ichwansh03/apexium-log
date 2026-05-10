package com.observability.sfdc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching
@EnableScheduling
@SpringBootApplication
class SfdcApplication

fun main(args: Array<String>) {
	runApplication<SfdcApplication>(*args)
}
