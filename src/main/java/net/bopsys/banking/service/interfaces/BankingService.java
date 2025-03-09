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

package net.bopsys.banking.service.interfaces;

import net.bopsys.banking.exception.BankAccountNotFoundException;
import net.bopsys.banking.model.dto.BankAccountCreateDto;
import net.bopsys.banking.model.dto.TransferOrderDto;
import net.bopsys.banking.model.entity.BankAccount;
import net.bopsys.banking.model.entity.TransferOrder;
import net.bopsys.banking.model.resource.BankAccountResource;
import net.bopsys.banking.model.resource.TransferOrderResource;

import java.util.List;

/**
 * @author Marco Ruiz
 */
public interface BankingService {

	String ACCOUNT_ROLE_DEBIT = "debit";
	String ACCOUNT_ROLE_CREDIT = "credit";

	/**
	 * Persists a new bank account record
	 *
	 * @param dto Details of the bank account to persist
	 * @return {@link BankAccountResource} persisted
	 */
	default BankAccountResource createAccount(BankAccountCreateDto dto) {
		return createAccount(dto.getCustomerId(), dto.getInitialBalanceInCents());
	}

	/**
	 * Persists a new bank account record
	 *
	 * @param customerId Identifier of the customer owning the bank account
	 * @param initialBalanceInCents Opening balance of the bank account to create
	 * @return {@link BankAccountResource} persisted
	 */
	BankAccountResource createAccount(Long customerId, long initialBalanceInCents);

	/**
	 * Retrieves a bank account
	 *
	 * @param accountId Identifier of the bank account to retrieve
	 * @return {@link BankAccountResource} of interest
	 * @throws BankAccountNotFoundException
	 */
	BankAccountResource getBankAccount(Long accountId) throws BankAccountNotFoundException;

	/**
	 * Persists a new transfer order and updates the balance of the affected accounts accordingly
	 *
	 * @param accountFromId Identifier of the bank account from which funds are going to be transferred
	 * @param dto Details of the transfer
	 * @return {@link TransferOrder} persisted
	 */
	TransferOrder createTransferOrder(Long accountFromId, TransferOrderDto dto);

	/**
	 * Computes the live balance of an account using its {@link BankAccount#getInitialBalanceInCents() initial balance}
	 * and its {@link #getTransferHistory(Long) transfer history}
	 *
	 * @param accountId Identifier of the bank account of interest
	 * @return Computed live balance
	 * @see BankAccount#getInitialBalanceInCents()
	 * @see #getTransferHistory(Long)
	 */
	long computeBalanceInCents(Long accountId);

	/**
	 * Retrieves all transfer order records where an account of interest has participated
	 *
	 * @param accountId Identifier of the bank account of interest
	 * @return List of {@link TransferOrderResource} associated with the account of interest
	 */
	List<TransferOrderResource> getTransferHistory(Long accountId);
}
