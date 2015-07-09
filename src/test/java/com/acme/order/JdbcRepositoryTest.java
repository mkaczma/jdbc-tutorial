package com.acme.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.acme.order.application.SpringAnnotationBasedApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAnnotationBasedApplication.class)
public class JdbcRepositoryTest {

	@Autowired
	JdbcOrderRepository jdbcOrderRepository;

	@Test
	public void findByOrderStatusTest() {
		jdbcOrderRepository.findByOrderStatus(OrderStatus.CREATED);
	}

	@Test
	public void getByPizzaOrderId() {
		jdbcOrderRepository.get("LARGE");
	}

	@Test
	public void findAllTest() {
		jdbcOrderRepository.findAll();
	}

}
