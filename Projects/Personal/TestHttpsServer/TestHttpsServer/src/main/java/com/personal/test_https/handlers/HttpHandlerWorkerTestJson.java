package com.personal.test_https.handlers;

import java.util.HashMap;
import java.util.Map;

import com.utils.http.server.AbstractHttpHandlerWorker;
import com.utils.io.ResourceFileUtils;

public class HttpHandlerWorkerTestJson
		extends AbstractHttpHandlerWorker implements HttpHandlerWorkerTest {

	@Override
	public String getName() {
		return "json";
	}

	@Override
	public void work() {

		final Map<String, String> responseHeaderMap = new HashMap<>();
		responseHeaderMap.put("Access-Control-Allow-Origin", "*");
		setResponseHeaderMap(responseHeaderMap);

		final String jsonString = ResourceFileUtils.resourceFileToString(
				"com/personal/test_https/handlers/test.json");
		setResponseBody(jsonString);
	}
}
