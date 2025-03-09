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

import net.bopsys.banking.exception.CustomerNotFoundException;
import net.bopsys.banking.model.dto.BankAccountCreateDto;
import net.bopsys.banking.model.dto.TransferOrderDto;
import net.bopsys.banking.model.entity.BankAccount;
import net.bopsys.banking.model.entity.Customer;
import net.bopsys.banking.model.resource.BankAccountResource;
import net.bopsys.banking.repository.BankAccountRepository;
import net.bopsys.banking.repository.CustomerRepository;
import net.bopsys.banking.repository.TransferOrderRepository;
import net.bopsys.banking.service.interfaces.BankingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

/**
 * @author Marco Ruiz
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BankingServiceImplTest {

	@Mock
	private BankAccountRepository bankAccountRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TransferOrderRepository transferOrderRepository;

	private BankingService bankingService;

	@BeforeEach
	void setupBase() {
		bankingService = new BankingServiceImpl(bankAccountRepository, customerRepository, transferOrderRepository);
	}

	@ParameterizedTest(name = "[{index}] => ({arguments}) : (givenCustomerId, givenCustomerName, givenInitialBalanceInCents)")
	@MethodSource
	void whenCreatingABankAccount_thenShouldReturnAppropriateBankAccountResource(
			long givenCustomerId, String givenCustomerName, long givenInitialBalanceInCents) {

		// WHEN
		Mockito.when(customerRepository.findCustomer(givenCustomerId))
				.thenAnswer(inv -> new Customer(givenCustomerId, givenCustomerName));
		Mockito.when(bankAccountRepository.saveAndFlush(Mockito.any()))
				.thenAnswer(inv -> inv.getArguments()[0]);

		BankAccountCreateDto dto = new BankAccountCreateDto(givenCustomerId, givenInitialBalanceInCents);
		BankAccountResource actualCustomerResource = bankingService.createAccount(dto);

		// THEN
		assertEquals(Duration.between(actualCustomerResource.getCreatedOn(), Instant.now()).toMinutes(), 0);
		assertEquals(actualCustomerResource.getName(), givenCustomerName);
		assertEquals(actualCustomerResource.getBalanceInCents(), givenInitialBalanceInCents);
		assertEquals(actualCustomerResource.getBalance(), givenInitialBalanceInCents / 100.00);

		Mockito.verify(customerRepository, times(1)).findCustomer(givenCustomerId);
	}

	private static Stream<Arguments> whenCreatingABankAccount_thenShouldReturnAppropriateBankAccountResource() {
		return Stream.of(
				Arguments.of(1L, "Alpha", 1000),
				Arguments.of(44L, "Beta", 555),
				Arguments.of(731L, "Gamma", 986554321),
				Arguments.of(8L, "Delta", 334409)
		);
	}

	@ParameterizedTest(name = "[{index}] => ({arguments}) : (givenCustomerId, givenInitialBalanceInCents)")
	@MethodSource
	void whenCreatingABankAccountForNonExistentUser_thenShouldThrowCustomerNotFoundException(
		long givenCustomerId, long givenInitialBalanceInCents) {

		// WHEN
		Mockito.when(customerRepository.findCustomer(Mockito.any()))
				.thenCallRealMethod();
		Mockito.when(customerRepository.findById(givenCustomerId))
				.thenAnswer(inv -> Optional.empty());

		BankAccountCreateDto dto = new BankAccountCreateDto(givenCustomerId, givenInitialBalanceInCents);

		// THEN
		assertThrows(CustomerNotFoundException.class, () -> bankingService.createAccount(dto));
		Mockito.verify(customerRepository, times(1)).findCustomer(givenCustomerId);
	}

	public static Stream<Arguments> whenCreatingABankAccountForNonExistentUser_thenShouldThrowCustomerNotFoundException() {
		return Stream.of(
				Arguments.of(1L, 1000),
				Arguments.of(44L, 555),
				Arguments.of(731L, 986554321),
				Arguments.of(8L, 334409)
		);
	}

	@ParameterizedTest(name = "[{index}] => ({arguments}) : (givenCustomerId, givenInitialBalanceInCents)")
	@MethodSource
	void whenTransferAmount_thenRespectiveBalancesShouldBeCreditedAndDebited(
			long transferAmountInCents, TransferDetails accountFrom, TransferDetails accountTo) {

		// WHEN
		Mockito.when(bankAccountRepository.findBankAccount(accountFrom.id, BankingService.ACCOUNT_ROLE_DEBIT))
				.thenAnswer(inv -> new BankAccount(accountFrom.id, new Customer(1L, ""), accountFrom.startingBalance));
		Mockito.when(bankAccountRepository.findBankAccount(accountTo.id, BankingService.ACCOUNT_ROLE_CREDIT))
				.thenAnswer(inv -> new BankAccount(accountTo.id, new Customer(2L, ""), accountTo.startingBalance));

		bankingService.createTransferOrder(accountFrom.id, new TransferOrderDto(accountTo.id, transferAmountInCents));
		ArgumentCaptor<BankAccount> captor = ArgumentCaptor.forClass(BankAccount.class);

		// THEN
		Mockito.verify(bankAccountRepository, times(2)).save(captor.capture());

		List<BankAccount> capturedBankAccounts = captor.getAllValues();

		assertEquals(capturedBankAccounts.get(0).getId(), accountFrom.id);
		assertEquals(capturedBankAccounts.get(0).getBalanceInCents(), accountFrom.expectedEndingBalance);

		assertEquals(capturedBankAccounts.get(1).getId(), accountTo.id);
		assertEquals(capturedBankAccounts.get(1).getBalanceInCents(), accountTo.expectedEndingBalance);
	}

	public static  Stream<Arguments> whenTransferAmount_thenRespectiveBalancesShouldBeCreditedAndDebited() {
		return Stream.of(
				Arguments.of(50, new TransferDetails(11L, 1000, 950), new TransferDetails(22L, 1000, 1050)),
				Arguments.of(1, new TransferDetails(11L, 1000, 999), new TransferDetails(22L, 1000, 1001)),
				Arguments.of(1000, new TransferDetails(22L, 1001, 1), new TransferDetails(23L, 1000, 2000))
		);
	}

	static class TransferDetails {
		public final long id, startingBalance, expectedEndingBalance;

		TransferDetails(long id, long startingBalance, long expectedEndingBalance) {
			this.id = id;
			this.startingBalance = startingBalance;
			this.expectedEndingBalance = expectedEndingBalance;
		}
	}


	// ... more like the previous tests ... moooooooore
}