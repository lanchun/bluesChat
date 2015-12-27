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
 * 消息数据交互类
 * 
 * @author lanchun
 *
 */
public class MessageDao {
	private Connection conn = DBUtil.getConn();

	public void add(Message m) {
		String sql = "insert into t_message(id,sid,rid,content,time,isread) values(null,?,?,?,?,?)";
		/*String uuid = UUID.randomUUID().toString();
		String mid = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23)
				+ uuid.substring(24);*/
		Object[] params = {  m.getSender().getId(), m.getReceiver().getId(), m.getContent(),
				m.getTime(),m.getRead() };
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		try {
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
	}

	//取出发送给uid的消息
	public List<Message> loadMsg(int uid) {
		String sql = "select sid,rid,content,time from t_message where rid=? and isread = 0";

		Object[] params = {uid};
		PreparedStatement pstm = DBUtil.getPstmt(conn, sql, params);
		ResultSet rs = null;
		List<Message> msgList = new ArrayList<>();
		try {
			rs = pstm.executeQuery();
			Message m = null;
			while (rs.next()) {
				m = new Message(rs.getInt(1),rs.getInt(2),rs.getString(3), rs.getTimestamp(4));
				msgList.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstm);
			ConnectionPool.getInstence().releaseConnection(conn);
		}
		return msgList;
	}
}
