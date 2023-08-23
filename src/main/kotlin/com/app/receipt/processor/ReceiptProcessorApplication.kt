package com.app.receipt.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReceiptProcessorApplication

fun main(args: Array<String>) {
	runApplication<ReceiptProcessorApplication>(*args)
}
