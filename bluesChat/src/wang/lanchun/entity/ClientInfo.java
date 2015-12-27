package wang.lanchun.entity;

import java.net.Socket;
/**
 * ¿Í»§¶ËsocketÀà
 * @author lanchun
 *
 */
public class ClientInfo {
	private Socket s;
	private User user;
	
	public ClientInfo(Socket s, User user) {
		this.s = s;
		this.user = user;
	}
	public Socket getS() {
		return s;
	}
	public void setS(Socket s) {
		this.s = s;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
