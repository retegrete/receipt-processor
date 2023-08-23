package com.app.receipt.processor.controllers

import com.app.receipt.processor.models.PointsModel
import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.models.ResponseModel
import com.app.receipt.processor.services.TemporaryDataService
import com.app.receipt.processor.services.CalculatePointsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/receipts")
class ReceiptProcessorController(private val receiptService: TemporaryDataService, private val calculatePoints: CalculatePointsService) {

    @PostMapping("/process", produces = ["application/json"])
    fun process(@RequestBody receipt: ReceiptModel): ResponseEntity<ResponseModel> {
        /* This code snippet is checking if the `purchaseDate` field of the `receipt` object has a valid date format. */
        if (!isValidDateFormat(receipt.purchaseDate)) {
            return ResponseEntity.badRequest().body(ResponseModel("date format is invalid"))
        }

        /* This code snippet is checking if the `purchaseTime` field of the `receipt` object has a valid time format. */
        if (!isValidTimeFormat(receipt.purchaseTime)) {
            return ResponseEntity.badRequest().body(ResponseModel("time format is invalid"))
        }

        return try {
            receiptService.saveTemporaryData(receipt)
            ResponseEntity.status(HttpStatus.CREATED).body(receiptService.returnId((receipt)))
        } catch (e: Exception) {
            /* `e.printStackTrace()` is a method that prints the stack trace of the exception that occurred. It is used for
            debugging purposes to see the details of the exception. */
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel("not saved"))
        }
    }

    @GetMapping("/{id}/points", produces = ["application/json"])
    fun getPoints(@PathVariable id: String): ResponseEntity<PointsModel>{
        val points = PointsModel()
        return try {
            /* The code snippet is retrieving the receipt record from the `calculatePoints` service using the provided
            `id`. It then calculates the points for the receipt record using the `calculate` method of the
            `calculatePoints` service. Finally, it creates a `ResponseEntity` object with the calculated points and
            returns it with an HTTP status of OK (200). */
            val receiptRecord = calculatePoints.findRegistry(id.toLong())
            points.points = calculatePoints.calculate(receiptRecord)
            ResponseEntity(points, HttpStatus.OK)
        }catch (e: Exception){
            points.points = e.message.toString()
            ResponseEntity(points, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun isValidDateFormat(dateString: String): Boolean {
        /* The code snippet is defining a date format pattern using the `DateTimeFormatter` class. The pattern is set to
        "yyyy-MM-dd", which represents the format of a date string with the year, month, and day separated by hyphens. */
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            LocalDate.parse(dateString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun isValidTimeFormat(timeString: String): Boolean {
        /* The code snippet is defining a time format pattern using the `DateTimeFormatter` class. The pattern is set to
        "HH:mm", which represents the format of a time string with the hour and minute separated by a colon. */
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return try {
            LocalTime.parse(timeString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
}