/**
 * Copyright [2020-2022] INESC TEC and VIZLORE Labs Foundation
 *
 * This software is authored by:
 * Aleksandar Tomcic (VIZLORE Labs Foundation)
 * Fábio André Castanheira Luís Coelho (INESC TEC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.yahoo.ycsb.schemes;

public class CreateSmartConnectorNoLease {

	private String knowledgeBaseId;
	private String knowledgeBaseName;
	private String knowledgeBaseDescription;
	private Boolean reasonerEnabled;

	public String getKnowledgeBaseId() {
		return knowledgeBaseId;
	}

	public void setKnowledgeBaseId(String knowledgeBaseId) {
		this.knowledgeBaseId = knowledgeBaseId;
	}

	public String getKnowledgeBaseName() {
		return knowledgeBaseName;
	}

	public void setKnowledgeBaseName(String knowledgeBaseName) {
		this.knowledgeBaseName = knowledgeBaseName;
	}

	public String getKnowledgeBaseDescription() {
		return knowledgeBaseDescription;
	}

	public void setKnowledgeBaseDescription(String knowledgeBaseDescription) {
		this.knowledgeBaseDescription = knowledgeBaseDescription;
	}


	public Boolean getReasonerEnabled() {
		return reasonerEnabled;
	}

	public void setReasonerEnabled(Boolean reasonerEnabled) {
		this.reasonerEnabled = reasonerEnabled;
	}

	public CreateSmartConnectorNoLease() {
		super();
	}

	public CreateSmartConnectorNoLease(String knowledgeBaseId, String knowledgeBaseName, String knowledgeBaseDescription,
                                       Boolean reasonerEnabled) {
		super();
		this.knowledgeBaseId = knowledgeBaseId;
		this.knowledgeBaseName = knowledgeBaseName;
		this.knowledgeBaseDescription = knowledgeBaseDescription;
		this.reasonerEnabled = reasonerEnabled;
	}

	public String toString() {
		return knowledgeBaseId;
	}

}
