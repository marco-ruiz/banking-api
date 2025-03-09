/*
 * Copyright 2025 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bopsys.banking.service;

import net.bopsys.banking.exception.BankAccountNotFoundException;
import net.bopsys.banking.exception.RequestOpeningBankAccountWithNegativeBalanceException;
import net.bopsys.banking.exception.SameBankAccountTransferNotAllowedException;
import net.bopsys.banking.exception.TransferAmountTooLowException;
import net.bopsys.banking.model.dto.TransferOrderDto;
import net.bopsys.banking.model.entity.BankAccount;
import net.bopsys.banking.model.entity.Customer;
import net.bopsys.banking.model.entity.TransferOrder;
import net.bopsys.banking.model.resource.BankAccountResource;
import net.bopsys.banking.model.resource.TransferOrderResource;
import net.bopsys.banking.repository.BankAccountRepository;
import net.bopsys.banking.repository.CustomerRepository;
import net.bopsys.banking.repository.TransferOrderRepository;
import net.bopsys.banking.service.interfaces.BankingService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Marco Ruiz
 */
@Service
public class BankingServiceImpl implements BankingService {

	private final BankAccountRepository bankAccountRepository;
	private final CustomerRepository customerRepository;
	private final TransferOrderRepository transferOrderRepository;

	public BankingServiceImpl(
			BankAccountRepository bankAccountRepository,
			CustomerRepository bankCustomerRepository,
			TransferOrderRepository bankAccountAmountTransferRepository) {

		this.bankAccountRepository = bankAccountRepository;
		this.customerRepository = bankCustomerRepository;
		this.transferOrderRepository = bankAccountAmountTransferRepository;
	}

	@Override
	public BankAccountResource createAccount(Long customerId, long initialBalanceInCents) {
		if (initialBalanceInCents < 0)
			throw new RequestOpeningBankAccountWithNegativeBalanceException(initialBalanceInCents);

		Customer customer = customerRepository.findCustomer(customerId);
		BankAccount persistedBankAccount = bankAccountRepository.saveAndFlush(new BankAccount(customer, initialBalanceInCents));
		return buildBankAccountResource(persistedBankAccount);
	}

	@Override
	public TransferOrder createTransferOrder(Long accountFromId, TransferOrderDto dto) {
		if (accountFromId.equals(dto.getAccountToId()))
			throw new SameBankAccountTransferNotAllowedException(accountFromId);

		return createTransferOrder(
				bankAccountRepository.findBankAccount(accountFromId, ACCOUNT_ROLE_DEBIT),
				bankAccountRepository.findBankAccount(dto.getAccountToId(), ACCOUNT_ROLE_CREDIT),
				dto.getTransferAmountInCents()
		);
	}

	@Transactional
	private TransferOrder createTransferOrder(BankAccount accountFrom, BankAccount accountTo, long transferAmountInCents) {
		if (transferAmountInCents <= 0)
			throw new TransferAmountTooLowException(transferAmountInCents);

		accountFrom.addCentsToBalance(transferAmountInCents * -1);
		bankAccountRepository.save(accountFrom);

		accountTo.addCentsToBalance(transferAmountInCents);
		bankAccountRepository.save(accountTo);

		TransferOrder transfer = new TransferOrder(accountFrom, accountTo, transferAmountInCents);
		return transferOrderRepository.save(transfer);
	}

	@Override
	public BankAccountResource getBankAccount(Long accountId) throws BankAccountNotFoundException {
		return buildBankAccountResource(bankAccountRepository.findBankAccount(accountId));
	}

	@Override
	public long computeBalanceInCents(Long accountId) {
		long aggregatedTransferAmount = getTransferHistory(accountId).stream()
				.mapToLong(t -> t.getTransferAmountInCents(accountId))
				.sum();
		return bankAccountRepository.findBankAccount(accountId).getInitialBalanceInCents() + aggregatedTransferAmount;
	}

	@Override
	public List<TransferOrderResource> getTransferHistory(Long accountId) {
		return transferOrderRepository.findBankAccountTransfers(accountId).stream()
				.map(this::transferOrderResource)
				.toList();
	}

	// RESOURCE BUILDERS

	private BankAccountResource buildBankAccountResource(BankAccount bankAccount) {
		return BankAccountResource.builder()
				.id(bankAccount.getId())
				.name(bankAccount.getCustomer().getName())
				.balanceInCents(bankAccount.getBalanceInCents())
				.createdOn(bankAccount.getCreatedOn())
				.build();
	}

	private TransferOrderResource transferOrderResource(
			TransferOrder bankAccountAmountTransfer) {

		return TransferOrderResource.builder()
				.id(bankAccountAmountTransfer.getId())
				.accountFromId(bankAccountAmountTransfer.getAccountFrom().getId())
				.accountToId(bankAccountAmountTransfer.getAccountTo().getId())
				.transferAmountInCents(bankAccountAmountTransfer.getAmountTransferredInCents())
				.createdOn(bankAccountAmountTransfer.getCreatedOn())
				.build();
	}
}
