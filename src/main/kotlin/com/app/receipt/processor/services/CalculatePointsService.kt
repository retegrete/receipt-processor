package com.app.receipt.processor.services

import com.app.receipt.processor.models.ItemDescription
import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.respositories.ReceiptDataRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.ceil

@Service
class CalculatePointsService(private val receiptDataRepository: ReceiptDataRepository) {

    fun findRegistry(id: Long): Optional<ReceiptModel> {
        return receiptDataRepository.findById(id)
    }
    fun calculate(data: Optional<ReceiptModel>): String {
        var points = 0
        val parsedDate = LocalDate.parse(data.get().purchaseDate)
        val dayOfMonth = parsedDate.dayOfMonth
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val parsedPurchaseTime = LocalTime.parse(data.get().purchaseTime, formatter)
        points += countAlphaNumeric(data.get().retailer)
        points += determineCents(data.get().total)
        points += findMultipleOfPointTwentyFive(data.get().total)
        points += countEveryTwoItems(data.get().items.size)
        points += findTrimmedLengthValue(data.get().items)
        points += findOddDay(dayOfMonth)
        points += findIfTimeOfPurchaseBetweenHours(parsedPurchaseTime)
        return points.toString()
    }

    private fun countAlphaNumeric(retailerName: String): Int {
        return retailerName.count { it.isLetterOrDigit() }
    }

    private fun determineCents(total: String): Int{
        if (total.contains(".00")){
            return 50
        }
        return 0
    }

    private fun findMultipleOfPointTwentyFive(total: String): Int{
        val value = total.toDoubleOrNull()
        if(value != null && value % 0.25 == 0.0){
            return 25
        }
        return 0
    }

    private fun countEveryTwoItems(itemSize: Int): Int{
        val multiplier = 5
        val twoItems = itemSize / 2
        return multiplier * twoItems

    }

    private fun findTrimmedLengthValue(items: List<ItemDescription>): Int {
        var result = 0.0
        for (item in items){
            if (item.shortDescription.trim().length % 3 == 0 ){
                val priceCalculation = item.price.toDouble() * 0.2
                result += ceil(priceCalculation)
            }
        }
        return result.toInt()
    }

    private fun findOddDay(day:Int): Int{
        if (day % 2 != 0){
            return 6
        }
        return 0
    }

    private fun findIfTimeOfPurchaseBetweenHours(purchaseTime: LocalTime): Int{
        val startTime = LocalTime.of(14, 0)
        val endTime = LocalTime.of(16, 0)
        if (purchaseTime.isAfter(startTime) && purchaseTime.isBefore(endTime)){
            return 10
        }
        return 0
    }
}