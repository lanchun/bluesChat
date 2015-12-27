package wang.lanchun.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import wang.lanchun.entity.Message;
import wang.lanchun.entity.User;
import wang.lanchun.utils.ConnectionPool;
import wang.lanchun.utils.DBUtil;

/**
 * UserDao用来和数据库交互
 * 
 * @author lanchun
 *
 */
public class UserDao {

	// 通过id来获得User对象
	public User getUser(int id) {
		Connection conn = DBUtil.getConn();
		String sql = "select username,online from t_user where id = ?";
		System.out.println(id);
		Object[] params = { id };
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		ResultSet rs = null;
		User user = new User(id);
		try {
			rs = pstm.executeQuery();
			if (rs.next()) {
				user.setNickname(rs.getString(1));
				user.setOnline(rs.getInt(2) == 0 ? false : true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return user;
	}

	// 改变上下线状态
	public void switchOnline(int uid, int status) {
		Connection conn = DBUtil.getConn();
		String sql = "update t_user set online = ? where id = ?";
		Object[] params = { status, uid };
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		try {
			pstm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("改变登录状态出错");
		} finally {
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
	}
	
	//登录
	public User login(String username,String password){
		User user = null;
		Connection conn = DBUtil.getConn();
		String sql = "select id,username from t_user where username=? and password = ?";
		Object[] params = {username,password};
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		ResultSet rs = null;
		try {
			rs = pstm.executeQuery();
			if (rs.next()) {
				user = new User(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return user;
	}
	
	//注册
	public boolean reg(String username,String password){
		boolean b = false;
		Connection conn = DBUtil.getConn();
		String sql = "insert into t_user(id,username,password,online) values(null,?,?,0)";
		Object[] params = {username,password};
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		try {
			int i = pstm.executeUpdate();
			if(i == 1){
				b = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException("改变登录状态出错");
		} finally {
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return b;
	}
	
	//获取所有用户
	public List<User> getAllUsers(){
		Connection conn = DBUtil.getConn();
		String sql = "select id,username from t_user";
		PreparedStatement ps = DBUtil.getPstmt(conn, sql);
		ResultSet rs = null;
		List<User> list = null;
		try {
			rs = ps.executeQuery();
			User user = null;
			list = new ArrayList<>();
			while(rs.next()){
				int uid = rs.getInt(1);
				String name = rs.getString(2);
				user = new User(uid,name);
				list.add(user);
			}
			user = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(rs);
			DBUtil.close(ps);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return list;
	}
	//查询用户在线状态
	public boolean isOnline(int uid){
		Connection conn = DBUtil.getConn();
		boolean b = false;
		String sql = "select online from t_user where id=?";
		Object[] param = {uid}; 
		PreparedStatement ps = DBUtil.getPstmt(conn, sql,param);
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getInt(1) == 1){
					b = true;
				}
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(rs);
			DBUtil.close(ps);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return b;
	}

}
