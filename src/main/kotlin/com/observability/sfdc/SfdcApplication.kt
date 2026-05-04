package com.observability.sfdc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SfdcApplication

fun main(args: Array<String>) {
	runApplication<SfdcApplication>(*args)
}
