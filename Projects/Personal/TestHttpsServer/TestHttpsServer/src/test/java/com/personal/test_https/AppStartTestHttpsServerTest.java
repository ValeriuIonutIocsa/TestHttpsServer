package com.personal.test_https;

import org.junit.jupiter.api.Test;

import com.utils.test.TestInputUtils;

class AppStartTestHttpsServerTest {

	@Test
	void testMain() {

		final String[] args;
		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {
			args = new String[] {
					"--hostname=localhost",
					"--port=8000"
			};
		} else {
			throw new RuntimeException();
		}

		AppStartTestHttpsServer.main(args);
	}
}
