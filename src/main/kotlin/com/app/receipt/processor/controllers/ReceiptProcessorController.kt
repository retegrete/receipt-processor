package com.app.receipt.processor.controllers

import com.app.receipt.processor.models.PointsModel
import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.models.ResponseModel
import com.app.receipt.processor.services.TemporaryDataService
import com.app.receipt.processor.services.CalculatePointsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/receipts")
class ReceiptProcessorController(private val receiptService: TemporaryDataService, private val calculatePoints: CalculatePointsService) {
    @PostMapping("/process", produces = ["application/json"])
    fun process(@RequestBody receipt: ReceiptModel): ResponseEntity<ResponseModel> {
        var response = ResponseModel()
        return try {
            receiptService.saveTemporaryData(receipt)
            response = receiptService.returnId((receipt))
            ResponseEntity(response, HttpStatus.OK)
        }catch (e: Exception){
            e.printStackTrace()
            response.id = "not saved"
            ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @GetMapping("/{id}/points", produces = ["application/json"])
    fun getPoints(@PathVariable id: String): ResponseEntity<PointsModel>{
        val points = PointsModel()
        return try {
            val receiptRecord = calculatePoints.findRegistry(id.toLong())
            points.points = calculatePoints.calculate(receiptRecord)
            ResponseEntity(points, HttpStatus.OK)
        }catch (e: Exception){
            points.points = e.message.toString()
            ResponseEntity(points, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}