package wang.lanchun.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

public class ConnectionPool {

	private String user;

	private String password;

	private String ip;

	private String port;

	private String dbName;

	private String url;

	private int poolSize;

	private int maxPoolSize;

	// 已经分配的连接数
	private static int size;

	private LinkedList<Connection> connectionPool;

	// 单例模式保证拿到的只有一个连接池
	private static class SingletonHolder {
		private static final ConnectionPool instance = new ConnectionPool();
	}

	public static ConnectionPool getInstence() {
		return SingletonHolder.instance;
	}

	private ConnectionPool() {
		initConnFromProp();
		initPool();
	}

	private void initConnFromProp() {
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream(
					"db.properties");
			Properties proHelper = new Properties();
			proHelper.load(in);

			ip = proHelper.getProperty("dburl");
			port = proHelper.getProperty("dbport");
			user = proHelper.getProperty("dbuser");
			password = proHelper.getProperty("dbpass");
			dbName = proHelper.getProperty("dbName");
			poolSize = Integer.parseInt(proHelper.getProperty("poolSize"));
			maxPoolSize = Integer
					.parseInt(proHelper.getProperty("maxPoolSize"));

			url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName
					+ "?useUnicode=true&characterEncoding=UTF-8";
			Class.forName("com.mysql.jdbc.Driver");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initPool() {
		connectionPool = new LinkedList<>();
		Connection conn = null;
		for (int i = 0; i < poolSize; i++) {
			try {
				conn = DriverManager.getConnection(url, user, password);
				connectionPool.add(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		print();
	}

	private void print() {
		System.out.println("数据库连接池数量:" + connectionPool.size());
	}

	public Connection getConnection() {
		Connection con = null;
		synchronized (this) {
			if (connectionPool.size() > 0) {
				con = connectionPool.poll();
				size++;
			} else if (connectionPool.size() + size < maxPoolSize) {
				try {
					con = DriverManager.getConnection(url, user, password);
					size++;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException("数据库连接数" + size + "！请稍后再试");
			}
		}
		System.out.println("已经分配的连接数:" + size);
		
		print();
		return con;
	}

	public void releaseConnection(Connection conn) {
		if (conn != null) {
			synchronized (this) {
				if (connectionPool.size() < poolSize) {
					connectionPool.add(conn);
					System.out.println("放回连接");
				} else {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				--size;
			}
		}
		print();
	}

}
