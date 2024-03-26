package com.personal.test_https;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.personal.test_https.handlers.HttpHandlerWorkerTest;
import com.personal.test_https.handlers.HttpHandlerWorkerTestJson;
import com.personal.test_https.handlers.HttpHandlerWorkerTestRoot;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import com.utils.concurrency.ThreadUtils;
import com.utils.http.server.HttpHandlerImpl;
import com.utils.http.server.HttpHandlerWorker;
import com.utils.io.ResourceFileUtils;
import com.utils.log.Logger;

final class TestHttpsServer {

	private TestHttpsServer() {
	}

	static void startServer(
            final String hostname,
            final int port) {

		final String serverName = "TestHttpsServer";
		try {
			Logger.printProgress("starting " + serverName);

			// set up the socket address
			final InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);

			// initialise the HTTPS server
			final int backlog = 0;
			final HttpsServer httpsServer = HttpsServer.create(inetSocketAddress, backlog);
			final SSLContext sslContext = SSLContext.getInstance("TLS");

			// initialise the keystore
			final char[] password = "CROCROCRO".toCharArray();
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			try (InputStream inputStream = ResourceFileUtils.resourceFileToInputStream(
					"com/personal/test_https/test_key.jks")) {

				keyStore.load(inputStream, password);
			}

			// set up the key manager factory
			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keyStore, password);

			// set up the trust manager factory
			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(keyStore);

			// set up the HTTPS context and parameters
			final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
			final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			sslContext.init(keyManagers, trustManagers, null);

			final HttpsConfigurator httpsConfigurator = new CustomHttpsConfigurator(sslContext);
			httpsServer.setHttpsConfigurator(httpsConfigurator);

			final HttpHandlerWorkerTest[] httpHandlerWorkerTestArray = {
					new HttpHandlerWorkerTestRoot(),
					new HttpHandlerWorkerTestJson()
			};
			for (final HttpHandlerWorker httpHandlerWorker : httpHandlerWorkerTestArray) {

				final String name = httpHandlerWorker.getName();
				final String path = "/" + name;
				final HttpHandler httpHandler = new HttpHandlerImpl(httpHandlerWorker, true);
				httpsServer.createContext(path, httpHandler);
			}

			httpsServer.setExecutor(Executors.newFixedThreadPool(12));
			httpsServer.start();

			final String url = "https://" + hostname + ":" + port + "/";
			Logger.printStatus(serverName + " has started at: " + url);
			ThreadUtils.trySleep(Long.MAX_VALUE);

		} catch (final Exception exc) {
			Logger.printError("failed to create " + serverName);
			Logger.printException(exc);
		}
	}
}
