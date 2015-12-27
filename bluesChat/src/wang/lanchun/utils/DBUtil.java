package wang.lanchun.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	
	public static Connection getConn(){
		return ConnectionPool.getInstence().getConnection();
	}
	
	public static PreparedStatement getPstmt(Connection conn,String sql,Object[] o){
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
			for(int i = 0;i < o.length;i++){
				pStmt.setObject(i+1,o[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pStmt;
	}
	
	public static PreparedStatement getPstmt(Connection conn,String sql){
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pStmt;
	}

	public static ResultSet getResultSet(PreparedStatement ps) {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
