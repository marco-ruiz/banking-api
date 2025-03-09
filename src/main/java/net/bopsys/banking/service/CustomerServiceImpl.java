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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bopsys.banking.model.dto.CustomerDto;
import net.bopsys.banking.model.entity.Customer;
import net.bopsys.banking.model.resource.CustomerResource;
import net.bopsys.banking.repository.CustomerRepository;
import net.bopsys.banking.service.interfaces.CustomerService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Marco Ruiz
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final ObjectMapper objectMapper;

	public CustomerServiceImpl(CustomerRepository customerRepository, ObjectMapper objectMapper) {
		this.customerRepository = customerRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public List<CustomerResource> createCustomers(String customersJson) throws JsonProcessingException {
		List<CustomerDto> dtos = objectMapper.readValue(customersJson, new TypeReference<>() {});
		return createCustomers(dtos);
	}

	@Override
	public List<CustomerResource> createCustomers(List<CustomerDto> dtos) {
		List<Customer> customers = dtos.stream().map(Customer::new).toList();
		List<Customer> persistedCustomers = customerRepository.saveAllAndFlush(customers);
		return persistedCustomers.stream().map(this::buildCustomerResource).toList();
	}

	@Override
	public CustomerResource createCustomer(CustomerDto dto) {
		return buildCustomerResource(customerRepository.save(new Customer(dto)));
	}

	@Override
	public CustomerResource getCustomer(Long customerId) {
		return buildCustomerResource(customerRepository.findCustomer(customerId));
	}

	@Override
	public List<CustomerResource> getCustomers() {
		return customerRepository.findAll().stream().map(this::buildCustomerResource).toList();
	}

	// RESOURCE BUILDERS

	private CustomerResource buildCustomerResource(Customer customer) {
		return CustomerResource.builder()
				.id(customer.getId())
				.name(customer.getName())
				.build();
	}
}
