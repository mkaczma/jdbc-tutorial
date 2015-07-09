package com.acme.order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.acme.order.pizza.PizzaOrder;
import com.acme.order.pizza.PizzaType;

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

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDateSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public String save(PizzaOrder order) {
		final String INSERT_ORDER_SQL = "INSERT INTO order_t (customer_id,status,type,estimatedDeliveryTime,finishTime) VALUES (?,?,?,?,?)";
		final String INSERT_CUSTOMER_SQL = "INSERT INTO customer_t (name,email,address) VALUES (?,?,?)";
		final String UPDATE_SQL = "UPDATE customer_t SET name=?,email=?,address=? WHERE id = ?";

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
		PizzaOrder pizzaOrder = null;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, pizzaOrderId);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				String customerId = result.getString("order_id");
				log.info("Customer id: " + customerId);
				String type = result.getString("type");
				log.info("type: " + type);
				String name = result.getString("name");
				log.info("name: " + name);
				String email = result.getString("email");
				log.info("email: " + email);
				String address = result.getString("address");
				log.info("address: " + address);
				Customer customer = new Customer(customerId, name, email, address);
				pizzaOrder = new PizzaOrder(customer, PizzaType.valueOf(type));
			}
		} catch (SQLException e) {
			log.error("SQL error", e);
			throw new RuntimeException("Error while fetching order by PizzaOrderId", e);
		}
		return pizzaOrder;
	}

	@Override
	public List<PizzaOrder> findAll() {
		final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id,o.status,o.type,o.estimatedDeliveryTime,"
				+ "o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id";
		List<PizzaOrder> pizzaOrders = new ArrayList<>();
		try (Connection conn = getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				String customerId = result.getString("order_id");
				String type = result.getString("type");
				String name = result.getString("name");
				String email = result.getString("email");
				String address = result.getString("address");
				Customer customer = new Customer(customerId, name, email, address);
				pizzaOrders.add(new PizzaOrder(customer, PizzaType.valueOf(type)));
			}
		} catch (SQLException e) {
			log.error("SQL error", e);
			throw new RuntimeException("Error while fetching all orders", e);
		}
		log.info("Find all list size: " + pizzaOrders.size());
		return pizzaOrders;
	}

	@Override
	public List<PizzaOrder> findByOrderStatus(OrderStatus orderStatus) {
		final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id,o.status,o.type,o.estimatedDeliveryTime,"
				+ "o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id and o.status = ?";
		List<PizzaOrder> pizzaOrders = this.jdbcTemplate.query(SQL, new RowMapper<PizzaOrder>() {
			@Override
			public PizzaOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
				String customerId = rs.getString("order_id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String address = rs.getString("address");
				Customer customer = new Customer(customerId, name, email, address);
				PizzaOrder pizzaOrder = new PizzaOrder(customer, PizzaType.valueOf(rs.getString("type")));
				return pizzaOrder;
			}

		}, orderStatus.name());
		log.info("findByOrderStatus list size: " + pizzaOrders.size());
		return pizzaOrders;
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
