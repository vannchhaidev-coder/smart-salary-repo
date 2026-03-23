package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.request.WalletTransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.WalletTransactionResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanTransactionResponse;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {

  @Mapping(target = "loanId", source = "referenceId")
  @Mapping(target = "type", expression = "java(mapType(txn.getTransactionType()))")
  @Mapping(target = "date", source = "createdAt")
  LoanTransactionResponse toLoanDto(WalletTransactionModel txn);

  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "wallet", source = "wallet")
  @Mapping(target = "amount", source = "request.amount")
  @Mapping(target = "transactionType", source = "request.transactionType")
  @Mapping(target = "referenceType", source = "request.referenceType")
  @Mapping(target = "referenceId", source = "request.referenceId")
  @Mapping(target = "description", source = "request.description")
  WalletTransactionModel toEntity(WalletTransactionRequest request, WalletModel wallet);

  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "wallet", source = "wallet")
  @Mapping(target = "transactionType", source = "request.type")
  @Mapping(target = "amount", source = "request.amount")
  @Mapping(target = "description", source = "request.description")
  @Mapping(
          target = "transactionDate",
          expression = "java(request.getTransactionDate() != null ? request.getTransactionDate() : java.time.LocalDateTime.now())"
  )
  @Mapping(target = "referenceId", ignore = true)
  @Mapping(target = "referenceType", ignore = true)
  WalletTransactionModel toWalletTransaction(TransactionRequest request, WalletModel wallet);

  @Mapping(target = "id", source = "uuid")
  WalletTransactionResponse toTransactionResponse(WalletTransactionModel transaction);

  @Mapping(target = "amount", source = "amount")
  @Mapping(target = "referenceId", source = "uuid")
  @Mapping(target = "transactionType", constant = "CREDIT")
  @Mapping(target = "referenceType", constant = "LOAN")
  @Mapping(target = "description", constant = "Loan disbursement")
  WalletTransactionRequest toWalletRequest(LoanModel loan);

  default String mapType(TransactionType type) {
    return type == TransactionType.DEBIT ? "deduction" : "credit";
  }

  default LocalDate map(Instant value) {
    return value == null ? null : value.atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
