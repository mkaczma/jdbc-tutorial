package com.acme.order;

import org.junit.Test;

public class JdbcRepositoryTest {

	JdbcOrderRepository jdbcOrderRepository = new JdbcOrderRepository();

	@Test
	public void findAllTest() {
		jdbcOrderRepository.findAll();

	}

}
