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

package net.bopsys.banking.model.resource;

import net.bopsys.banking.exception.BankAccountNotFoundException;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marco Ruiz
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransferOrderResource {

	private UUID id;
	private Long accountFromId;
	private Long accountToId;

	// In cent amounts. If value = 2000, then amount transferred = 20.00
	private long transferAmountInCents;

	private Instant createdOn;

	public Long getTransferAmountInCents(Long accountId) {
		if (!accountId.equals(accountFromId) && !accountId.equals(accountToId))
			throw new BankAccountNotFoundException(accountId);

		int multiplier = accountId.equals(accountToId) ? 1 : -1;
		return transferAmountInCents * multiplier;
	}
}
