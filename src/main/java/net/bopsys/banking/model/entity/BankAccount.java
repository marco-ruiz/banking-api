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

import net.bopsys.banking.exception.InsufficientBalanceForTransferException;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "id", updatable = false)
	private Customer customer;

	// In cent amounts. If value = 2000, then balance = 20.00
	@Column(updatable = false)
	private long initialBalanceInCents;

	// In cent amounts. If value = 2000, then balance = 20.00
	private long balanceInCents;

	@Column(updatable = false)
	private Instant createdOn;

	// NOT IDEAL: ADDED FOR QUICK TESTING PURPOSES
	public BankAccount(long id, Customer customer, long initialBalanceInCents) {
		this(customer, initialBalanceInCents);
		this.id = id;
	}

	public BankAccount(Customer customer, long initialBalanceInCents) {
		this.customer = customer;
		this.initialBalanceInCents = initialBalanceInCents;
		this.balanceInCents = initialBalanceInCents;
		this.createdOn = Instant.now();
	}

	public void addCentsToBalance(long cents) throws InsufficientBalanceForTransferException {
		if (balanceInCents + cents < 0)
			throw new InsufficientBalanceForTransferException(id, balanceInCents, cents);

		balanceInCents += cents;
	}
}
