package com.personal.test_https.handlers;

import java.util.HashMap;
import java.util.Map;

import com.utils.http.server.AbstractHttpHandlerWorker;

public class HttpHandlerWorkerTestRoot
		extends AbstractHttpHandlerWorker implements HttpHandlerWorkerTest {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void work() {

		final Map<String, String> responseHeaderMap = new HashMap<>();
		responseHeaderMap.put("Access-Control-Allow-Origin", "*");
		setResponseHeaderMap(responseHeaderMap);

		setResponseBody("Test HTTP Server");
	}
}
