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

import com.fasterxml.jackson.core.JsonProcessingException;

import net.bopsys.banking.model.dto.CustomerDto;
import net.bopsys.banking.model.resource.CustomerResource;

import java.util.List;

/**
 * @author Marco Ruiz
 */
public interface CustomerService {

	/**
	 * Persists customer records corresponding to the resources described by the JSON string passed as parameter
	 *
	 * @param customersJson JSON representation of the customer records to persist
	 * @return List of {@link CustomerResource} persisted
	 * @throws JsonProcessingException If the string passed as parameter cannot be translated to JSON list of {@link CustomerDto}
	 */
	List<CustomerResource> createCustomers(String customersJson) throws JsonProcessingException;

	/**
	 * Persists customer records corresponding to the resources described by the list of {@link CustomerDto}s passed as parameter
	 *
	 * @param dtos List of {@link CustomerDto} representing the customer records to persist
	 * @return List of {@link CustomerResource} persisted
	 */
	default List<CustomerResource> createCustomers(List<CustomerDto> dtos) {
		return dtos.stream().map(this::createCustomer).toList();
	}

	/**
	 * Persist a customer record corresponding to the resource described by the {@link CustomerDto} passed as parameter
	 *
	 * @param dto {@link CustomerDto} representing the customer record to persist
	 * @return {@link CustomerResource} persisted
	 */
	CustomerResource createCustomer(CustomerDto dto);

	/**
	 * Retrieves a {@link CustomerResource} given its id
	 *
	 * @param customerId Identifier of the {@link CustomerResource} of interest
	 * @return {@link CustomerResource} of interest
	 */
	CustomerResource getCustomer(Long customerId);

	/**
	 * Retrieves all {@link CustomerResource}s persisted
	 *
	 * @return All {@link CustomerResource}s persisted
	 */
	List<CustomerResource> getCustomers();
}
