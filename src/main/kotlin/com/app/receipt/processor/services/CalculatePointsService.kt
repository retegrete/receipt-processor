package com.app.receipt.processor.services

import com.app.receipt.processor.models.ItemDescription
import com.app.receipt.processor.models.ReceiptModel
import com.app.receipt.processor.respositories.ReceiptDataRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.math.ceil

@Service
class CalculatePointsService(private val receiptDataRepository: ReceiptDataRepository) {

    /**
     * The function `findRegistry` returns an optional `ReceiptModel` by searching for it using the provided `id`.
     *
     * @param id The `id` parameter is of type `Long` and represents the unique identifier of a receipt in the registry.
     * @return The method `findRegistry` is returning an `Optional` object that contains a `ReceiptModel` object.
     */
    fun findRegistry(id: Long): Optional<ReceiptModel> {
        return receiptDataRepository.findById(id)
    }
    fun calculate(data: Optional<ReceiptModel>): String {
        /* The code snippet is calculating the points for a given receipt. */
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

    /**
     * The function "countAlphaNumeric" takes a string as input and returns the count of alphanumeric characters in the
     * string.
     *
     * @param retailerName The `retailerName` parameter is a string that represents the name of a retailer.
     * @return The count of alphanumeric characters in the retailerName string.
     */
    fun countAlphaNumeric(retailerName: String): Int {
        return retailerName.count { it.isLetterOrDigit() }
    }

    /**
     * The function "determineCents" returns 50 if the input string "total" contains ".00", otherwise it returns 0.
     *
     * @param total The `total` parameter is a string representing a monetary value.
     * @return an integer value. If the input string "total" contains ".00", the function returns 50. Otherwise, it returns
     * 0.
     */
    fun determineCents(total: String): Int{
        if (total.contains(".00")){
            return 50
        }
        return 0
    }

    /**
     * The function "findMultipleOfPointTwentyFive" checks if a given total is a multiple of 0.25 and returns 25 if it is,
     * otherwise it returns 0.
     *
     * @param total The `total` parameter is a string representing a numerical value.
     * @return an integer value. If the input `total` is a valid number and is divisible by 0.25, the function will return
     * 25. Otherwise, it will return 0.
     */
    fun findMultipleOfPointTwentyFive(total: String): Int{
        val value = total.toDoubleOrNull()
        if(value != null && value % 0.25 == 0.0){
            return 25
        }
        return 0
    }

    /**
     * The function counts the number of pairs of items and multiplies it by a constant value.
     *
     * @param itemSize The parameter `itemSize` represents the total number of items.
     * @return the result of multiplying the number of two items by the multiplier.
     */
    fun countEveryTwoItems(itemSize: Int): Int{
        val multiplier = 5
        val twoItems = itemSize / 2
        return multiplier * twoItems

    }

    /**
     * The function calculates the total price of items whose short description length is divisible by 3, and returns the
     * rounded integer value.
     *
     * @param items A list of ItemDescription objects. Each ItemDescription object has two properties: shortDescription (a
     * string) and price (a number).
     * @return an integer value.
     */
    fun findTrimmedLengthValue(items: List<ItemDescription>): Int {
        var result = 0.0
        for (item in items){
            if (item.shortDescription.trim().length % 3 == 0 ){
                val priceCalculation = item.price.toDouble() * 0.2
                result += ceil(priceCalculation)
            }
        }
        return result.toInt()
    }

    /**
     * The function "findOddDay" returns 6 if the input day is odd, otherwise it returns 0.
     *
     * @param day The parameter "day" is an integer representing a day of the week.
     * @return either 6 or 0, depending on whether the input day is odd or even.
     */
    fun findOddDay(day:Int): Int{
        if (day % 2 != 0){
            return 6
        }
        return 0
    }

    /**
     * The function checks if the given purchase time is between 2 PM and 4 PM and returns 10 if it is, otherwise it
     * returns 0.
     *
     * @param purchaseTime The parameter `purchaseTime` is of type `LocalTime`, which represents a time without a date or
     * time zone. It is used to represent the time of purchase.
     * @return an integer value. If the purchase time is between 2:00 PM and 4:00 PM, it returns 10. Otherwise, it returns
     * 0.
     */
    fun findIfTimeOfPurchaseBetweenHours(purchaseTime: LocalTime): Int{
        val startTime = LocalTime.of(14, 0)
        val endTime = LocalTime.of(16, 0)
        if (purchaseTime.isAfter(startTime) && purchaseTime.isBefore(endTime)){
            return 10
        }
        return 0
    }
}