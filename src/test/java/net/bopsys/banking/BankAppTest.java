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

package net.bopsys.banking;

import net.bopsys.banking.controller.BankingController;
import net.bopsys.banking.controller.CustomerController;
import net.bopsys.banking.repository.BankAccountRepository;
import net.bopsys.banking.repository.CustomerRepository;
import net.bopsys.banking.repository.TransferOrderRepository;
import net.bopsys.banking.service.InitializerService;
import net.bopsys.banking.service.interfaces.BankingService;
import net.bopsys.banking.service.interfaces.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Marco Ruiz
 */
@SpringBootTest
class BankAppTest {

	@Autowired
	private BankingController bankingController;

	@Autowired
	private CustomerController customerController;

	@Autowired
	private BankingService bankingService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InitializerService initializerService;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransferOrderRepository transferOrderRepository;


	@Test
	void whenContextLoads_thenShouldAutowireANonNullBankingController() {
		assertThat(bankingController).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullCustomerController() {
		assertThat(customerController).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullBankingService() {
		assertThat(bankingService).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullCustomerService() {
		assertThat(customerService).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullInitializerService() {
		assertThat(initializerService).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullBankAccountRepository() {
		assertThat(bankAccountRepository).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullCustomerRepository() {
		assertThat(customerRepository).isNotNull();
	}

	@Test
	void whenContextLoads_thenShouldAutowireANonNullTransferOrderRepository() {
		assertThat(transferOrderRepository).isNotNull();
	}
}