package com.observability.sfdc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SfdcApplication

fun main(args: Array<String>) {
	runApplication<SfdcApplication>(*args)
}
