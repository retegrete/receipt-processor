package com.app.receipt.processor.respositories

import com.app.receipt.processor.models.ReceiptModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceiptDataRepository: JpaRepository<ReceiptModel, Long>