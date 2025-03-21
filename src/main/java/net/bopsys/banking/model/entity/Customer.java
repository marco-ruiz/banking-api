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

package net.bopsys.banking.model.entity;

import net.bopsys.banking.model.dto.CustomerDto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Marco Ruiz
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer {

	@Id
	private Long id;

	private String name;

	@OneToMany(mappedBy = "customer")
	private List<BankAccount> bankAccounts;

	public Customer(CustomerDto dto) {
		this(dto.getId(), dto.getName());
	}

	public Customer(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
