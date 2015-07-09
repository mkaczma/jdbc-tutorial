package com.acme.order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.acme.order.pizza.PizzaOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Primary
public class JdbcOrderRepository implements OrderRepository {

	private final String url = "jdbc:mysql://localhost:3306/pizza-tutorial";

	private final String user = "dbuser";

	private final String password = "dbpass";

	@Autowired
	private DataSource dataSource;

	@Override
	public String save(PizzaOrder order) {
		final String INSERT_SQL = "INSERT INTO order_t (customer_id,status,type,estimatedDeliveryTime,finishTime) VALUES (?,?,?,?,?)";
		try (Connection conn = dataSource.getConnection()) {

		} catch (SQLException e) {
			log.error("SQL error", e);
			throw new RuntimeException("Error while saving order", e);
		}
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public PizzaOrder get(String pizzaOrderId) {
		final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id,o.status,o.type,o.estimatedDeliveryTime,"
				+ "o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id and o.id = ?";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, pizzaOrderId);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				long orderId = result.getLong("order_id");
				log.info("Order id: " + orderId);
				long customerId = result.getLong("order_id");
				log.info("Customer id: " + customerId);
				String status = result.getString("status");
				log.info("Status: " + status);
				String type = result.getString("type");
				log.info("type: " + type);
				String estimatedDeliveryTime = result.getString("estimatedDeliveryTime");
				log.info("estimatedDeliveryTime: " + estimatedDeliveryTime);
				String finishTime = result.getString("finishTime");
				log.info("finishTime: " + finishTime);
				String name = result.getString("name");
				log.info("name: " + name);
				String email = result.getString("email");
				log.info("email: " + email);
				String address = result.getString("address");
				log.info("address: " + address);
			}
		} catch (SQLException e) {
			log.error("SQL error", e);
			throw new RuntimeException("Error while fetching order by PizzaOrderId", e);
		}
		return null;
	}

	@Override
	public List<PizzaOrder> findAll() {
		final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id,o.status,o.type,o.estimatedDeliveryTime,"
				+ "o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id";
		try (Connection conn = getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				long orderId = result.getLong("order_id");
				log.info("Order id: " + orderId);
				long customerId = result.getLong("order_id");
				log.info("Customer id: " + customerId);
				String status = result.getString("status");
				log.info("Status: " + status);
				String type = result.getString("type");
				log.info("type: " + type);
				String estimatedDeliveryTime = result.getString("estimatedDeliveryTime");
				log.info("estimatedDeliveryTime: " + estimatedDeliveryTime);
				String finishTime = result.getString("finishTime");
				log.info("finishTime: " + finishTime);
				String name = result.getString("name");
				log.info("name: " + name);
				String email = result.getString("email");
				log.info("email: " + email);
				String address = result.getString("address");
				log.info("address: " + address);
			}
		} catch (SQLException e) {
			log.error("SQL error", e);
			throw new RuntimeException("Error while fetching all orders", e);
		}

		return null;
	}

	@Override
	public List<PizzaOrder> findByOrderStatus(OrderStatus orderStatus) {

		return null;
	}

	private Connection getConnection() {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.user);
		connectionProps.put("password", this.password);
		try {
			conn = (Connection) DriverManager.getConnection(this.url, connectionProps);
		} catch (SQLException e) {
			log.info("Connection error", e);
			throw new RuntimeException("Error while connect to DB", e);
		}
		return conn;
	}

}
