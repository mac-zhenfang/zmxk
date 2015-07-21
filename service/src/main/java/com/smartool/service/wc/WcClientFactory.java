package com.smartool.service.wc;

import com.smartool.service.client.ClientFactory;

public class WcClientFactory extends ClientFactory {

	protected WcClientFactory(Builder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends ClientFactory.Builder<Builder> {

		public WcClientFactory build() {
			return new WcClientFactory(this);
		}
	}

}
