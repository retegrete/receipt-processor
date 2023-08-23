package com.app.receipt.processor.services

import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.models.ResponseModel
import com.app.receipt.processor.respositories.ReceiptDataRepository
import org.springframework.stereotype.Service

@Service
class TemporaryDataService(private val receiptDataRepository: ReceiptDataRepository) {

    fun saveTemporaryData(data: ReceiptModel): ReceiptModel{
        receiptDataRepository.save(data)
        return data
    }
    fun returnId(savedData: ReceiptModel): ResponseModel{
        val response = ResponseModel()
        response.id = savedData.id.toString()
        return response
    }
}