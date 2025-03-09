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

package net.bopsys.banking.controller;

import net.bopsys.banking.model.dto.CustomerDto;
import net.bopsys.banking.model.resource.CustomerResource;
import net.bopsys.banking.service.interfaces.CustomerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Marco Ruiz
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping("/")
	public CustomerResource createCustomer(@RequestBody CustomerDto dto) {
		return customerService.createCustomer(dto);
	}

	@GetMapping("/{id}")
	public CustomerResource getCustomer(@PathVariable Long id) {
		return customerService.getCustomer(id);
	}

	@GetMapping("/")
	public List<CustomerResource> getAccountBalance() {
		return customerService.getCustomers();
	}
}
