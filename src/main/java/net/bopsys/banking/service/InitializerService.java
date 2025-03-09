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

import net.bopsys.banking.service.interfaces.CustomerService;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * @author Marco Ruiz
 */
@Component
public class InitializerService {

	private final CustomerService customerService;

	public InitializerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostConstruct
	public void prepopulateCustomers() throws JsonProcessingException {
		customerService.createCustomers("""
				[
				  {
				    "id": 1,
				    "name": "Arisha Barron"
				  },
				  {
				    "id": 2,
				    "name": "Branden Gibson"
				  },
				  {
				    "id": 3,
				    "name": "Rhonda Church"
				  },
				  {
				    "id": 4,
				    "name": "Georgina Hazel"
				  }
				]
				""");
	}
}
