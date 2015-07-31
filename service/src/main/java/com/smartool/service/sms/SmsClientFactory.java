package com.smartool.service.sms;

import com.smartool.service.client.ClientFactory;

public class SmsClientFactory extends ClientFactory {
	protected SmsClientFactory(Builder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends ClientFactory.Builder<Builder> {

		public SmsClientFactory build() {
			return new SmsClientFactory(this);
		}
	}

}