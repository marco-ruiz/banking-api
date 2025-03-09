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

import com.fasterxml.jackson.core.JsonProcessingException;

import net.bopsys.banking.model.resource.CustomerResource;
import net.bopsys.banking.service.interfaces.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marco Ruiz
 */
@SpringBootTest
class InitializerServiceTest {

	@Autowired
	private CustomerService customerService;

	@Test
	void whenInitiallyPrepopulatingDb_thenDbShouldStoreExpectedRecords() throws JsonProcessingException {
		List<CustomerResource> expectedCustomers = List.of(
				new CustomerResource(1L, "Arisha Barron"),
				new CustomerResource(2L, "Branden Gibson"),
				new CustomerResource(3L, "Rhonda Church"),
				new CustomerResource(4L, "Georgina Hazel")
		);
		List<CustomerResource> actualCustomers = customerService.getCustomers();

		assertEquals(actualCustomers.size(), 4);
		assertThat(actualCustomers).containsAll(expectedCustomers);
	}
}