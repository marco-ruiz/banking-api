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

import java.time.Instant;
import java.util.UUID;

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
public class TransferOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "accountFromId", referencedColumnName = "id", updatable = false)
	private BankAccount accountFrom;

	@ManyToOne
	@JoinColumn(name = "accountToId", referencedColumnName = "id", updatable = false)
	private BankAccount accountTo;

	// In cent amounts. If value = 2000, then amount transferred = 20.00
	@Column(updatable = false)
	private long amountTransferredInCents;

	@Column(updatable = false)
	private Instant createdOn;

	public TransferOrder(BankAccount accountFrom, BankAccount accountTo, long amountTransferredInCents) {
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amountTransferredInCents = amountTransferredInCents;
		this.createdOn = Instant.now();
	}
}
