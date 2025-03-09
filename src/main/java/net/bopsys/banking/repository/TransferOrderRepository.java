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

package net.bopsys.banking.repository;

import net.bopsys.banking.model.entity.TransferOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Marco Ruiz
 */
public interface TransferOrderRepository extends JpaRepository<TransferOrder, UUID> {

	default List<TransferOrder> findBankAccountTransfers(Long accountId) {
		return findByAccountFromIdOrAccountToIdOrderByCreatedOn(accountId, accountId);
	}

	/**
	 * Retrieves the entities corresponding to all the amount transfers in which the bank account of interest is involved.
	 *
	 * @param accountFromId Identifier of the bank account of interest
	 * @param accountToId Identifier of the bank account of interest
	 * @return Entities corresponding to all the amount transfers in which the bank account of interest is involved.
	 *
	 * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html"></a>
	 */
	List<TransferOrder> findByAccountFromIdOrAccountToIdOrderByCreatedOn(Long accountFromId, Long accountToId);
}
