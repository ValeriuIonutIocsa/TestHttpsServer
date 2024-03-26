package com.personal.test_https;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.utils.log.Logger;

class CustomHttpsConfigurator extends HttpsConfigurator {

	CustomHttpsConfigurator(
			final SSLContext sslContext) {

		super(sslContext);
	}

	@Override
	public void configure(
			final HttpsParameters httpsParameters) {

		try {
			final SSLContext sslContext = getSSLContext();
			final SSLEngine engine = sslContext.createSSLEngine();
			httpsParameters.setNeedClientAuth(false);
			httpsParameters.setCipherSuites(engine.getEnabledCipherSuites());
			httpsParameters.setProtocols(engine.getEnabledProtocols());

			final SSLParameters sslParameters = sslContext.getSupportedSSLParameters();
			httpsParameters.setSSLParameters(sslParameters);

		} catch (final Exception exc) {
			Logger.printError("failed to configure HTTPS parameters");
			Logger.printException(exc);
		}
	}
}
