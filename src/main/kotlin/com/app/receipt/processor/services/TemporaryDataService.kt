package com.app.receipt.processor.services

import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.models.ResponseModel
import com.app.receipt.processor.respositories.ReceiptDataRepository
import org.springframework.stereotype.Service

@Service
class TemporaryDataService(private val receiptDataRepository: ReceiptDataRepository) {

    /**
     * The function saves temporary data and returns the ID of the saved data.
     *
     * @param data The "data" parameter is of type ReceiptModel, which represents the data of a receipt.
     * @return The function `returnId` is returning a `ResponseModel` object.
     */
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