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

package net.bopsys.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Marco Ruiz
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankAccountNotFoundException extends RuntimeException {

	private static String buildAccountRoleDescription(String accountRole) {
		return (accountRole == null || accountRole.isBlank()) ? "" : String.format("(%s account)", accountRole);
	}

	public BankAccountNotFoundException(Long bankAccountId) {
		this(bankAccountId, "");
	}

	public BankAccountNotFoundException(Long bankAccountId, String accountRole) {
		super(String.format("Bank account with id '%s' not found. %s",
				bankAccountId,
				buildAccountRoleDescription(accountRole))
		);
	}
}
