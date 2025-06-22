package com.letrasypapeles.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.letrasypapeles.backend.BackendApplication;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMain() {
		BackendApplication.main(new String[] {});
	}

}
