package wang.lanchun.entity;

import java.io.Serializable;
import java.net.Socket;

/**
 * 用户实体类
 * @author lanchun
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = -1896921950266137246L;
	
	//用户id号，用来登录
	private int id;
	
	//昵称
	private String nickname;
	
	//密码
	private String password;
	
	//是否在线
	private boolean online;
	
	public User(int uid) {
		this.id = uid;
	}

	public User(int uid, String name) {
		this(uid);
		this.nickname = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nickname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
