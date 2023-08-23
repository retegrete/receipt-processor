package com.app.receipt.processor

import com.app.receipt.processor.models.ItemDescription
import com.app.receipt.processor.respositories.ReceiptDataRepository
import com.app.receipt.processor.services.CalculatePointsService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalTime

@SpringBootTest
class ReceiptProcessorApplicationTests {
	val mockData = mock(ReceiptDataRepository::class.java)!!
	@Test
	fun testCountAlphaNumeric_OnlyAlphaNumeric() {
		val processor = CalculatePointsService(mockData)
		val result = processor.countAlphaNumeric("Example12")
		assertEquals(9, result)
	}

	@Test
	fun testCountAlphaNumeric_SpecialCharacters() {
		val processor = CalculatePointsService(mockData)
		val result = processor.countAlphaNumeric("Special\$Chars!")
		assertEquals(12, result)
	}

	@Test
	fun testCountAlphaNumeric_WithSpaces() {
		val processor = CalculatePointsService(mockData)
		val result = processor.countAlphaNumeric("Alpha Numeric 123")
		assertEquals(15, result)
	}

	@Test
	fun testCountAlphaNumeric_MixedCharacters() {
		val processor = CalculatePointsService(mockData)
		val result = processor.countAlphaNumeric("Mix3d Ch@racters")
		assertEquals(14, result)
	}

	@Test
	fun testDetermineCents_RoundDollarAmount() {
		val processor = CalculatePointsService(mockData)
		val result = processor.determineCents("100.00")
		assertEquals(50, result)
	}

	@Test
	fun testDetermineCents_WithCents() {
		val processor = CalculatePointsService(mockData)
		val result = processor.determineCents("99.99")
		assertEquals(0, result)
	}

	@Test
	fun testDetermineCents_NotRoundDollarAmount() {
		val processor = CalculatePointsService(mockData)
		val result = processor.determineCents("123.45")
		assertEquals(0, result)
	}

	@Test
	fun testFindMultipleOfPointTwentyFive_MultipleOf025() {
		val processor = CalculatePointsService(mockData)
		val result = processor.findMultipleOfPointTwentyFive("10.25")
		assertEquals(25, result)
	}

	@Test
	fun testFindMultipleOfPointTwentyFive_NotMultipleOf025() {
		val processor = CalculatePointsService(mockData)
		val result = processor.findMultipleOfPointTwentyFive("9.99")
		assertEquals(0, result)
	}

	@Test
	fun testCountEveryTwoItems_NoItems() {
		val processor = CalculatePointsService(mockData)
		val result = processor.countEveryTwoItems(0)
		assertEquals(0, result)
	}

	@Test
	fun testCountEveryTwoItems_OneItem() {
		val processor =  CalculatePointsService(mockData)
		val result = processor.countEveryTwoItems(1)
		assertEquals(0, result)
	}

	@Test
	fun testCountEveryTwoItems_EvenItemCount() {
		val processor =  CalculatePointsService(mockData)
		val result = processor.countEveryTwoItems(4)
		assertEquals(10, result)
	}

	@Test
	fun testCountEveryTwoItems_OddItemCount() {
		val processor =  CalculatePointsService(mockData)
		val result = processor.countEveryTwoItems(5)
		assertEquals(10, result)
	}

	@Test
	fun testFindTrimmedLengthValue_MultipleOf3() {
		val processor = CalculatePointsService(mockData)
		val itemDescriptions = listOf(
			ItemDescription("Description1", "10.00"),
			ItemDescription("Description2", "15.00")
		)
		val result = processor.findTrimmedLengthValue(itemDescriptions)
		assertEquals(5, result)
	}

	@Test
	fun testFindTrimmedLengthValue_NotMultipleOf3() {
		val processor = CalculatePointsService(mockData)
		val itemDescriptions = listOf(
			ItemDescription("Desc1", "10.00"),
			ItemDescription("Desc22", "15.00"),
			ItemDescription("Desc333", "20.00")
		)
		val result = processor.findTrimmedLengthValue(itemDescriptions)
		assertEquals(3, result)
	}

	@Test
	fun testFindOddDay_OddDay() {
		val processor = CalculatePointsService(mockData)
		val result = processor.findOddDay(13)
		assertEquals(6, result)
	}

	@Test
	fun testFindOddDay_EvenDay() {
		val processor = CalculatePointsService(mockData)
		val result = processor.findOddDay(20)
		assertEquals(0, result)
	}

	@Test
	fun testFindIfTimeOfPurchaseBetweenHours_Before2pm() {
		val processor = CalculatePointsService(mockData)
		val purchaseTime = LocalTime.of(12, 0)
		val result = processor.findIfTimeOfPurchaseBetweenHours(purchaseTime)
		assertEquals(0, result)
	}

	@Test
	fun testFindIfTimeOfPurchaseBetweenHours_Between2pmAnd4pm() {
		val processor = CalculatePointsService(mockData)
		val purchaseTime = LocalTime.of(14, 30)
		val result = processor.findIfTimeOfPurchaseBetweenHours(purchaseTime)
		assertEquals(10, result)
	}

	@Test
	fun testFindIfTimeOfPurchaseBetweenHours_After4pm() {
		val processor = CalculatePointsService(mockData)
		val purchaseTime = LocalTime.of(16, 30)
		val result = processor.findIfTimeOfPurchaseBetweenHours(purchaseTime)
		assertEquals(0, result)
	}

}
