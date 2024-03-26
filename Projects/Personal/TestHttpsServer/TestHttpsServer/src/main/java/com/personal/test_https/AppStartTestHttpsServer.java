package com.personal.test_https;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.cli.CliUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

final class AppStartTestHttpsServer {

	private AppStartTestHttpsServer() {
	}

	public static void main(
			final String[] args) {

		final Map<String, String> cliArgsByNameMap = new HashMap<>();
		CliUtils.fillCliArgsByNameMap(args, cliArgsByNameMap);

		boolean keepGoing = true;
		final String hostname = cliArgsByNameMap.get("hostname");
		if (StringUtils.isBlank(hostname)) {

			Logger.printError("missing \"hostname\" attribute");
			keepGoing = false;
		}

		int port = -1;
		if (keepGoing) {

			final String portString = cliArgsByNameMap.get("port");
			port = StrUtils.tryParsePositiveInt(portString);
			if (port < 0) {

				Logger.printError("missing or invalid \"port\" attribute");
				keepGoing = false;
			}
		}

		if (keepGoing) {
			TestHttpsServer.startServer(hostname, port);
		}
	}
}
