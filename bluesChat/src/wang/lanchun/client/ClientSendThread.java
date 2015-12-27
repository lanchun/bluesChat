package wang.lanchun.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;

import wang.lanchun.dao.UserDao;
import wang.lanchun.entity.Message;
import wang.lanchun.entity.User;

public class ClientSendThread implements Runnable {

	private Socket s;
	private User sender;
	private ObjectOutputStream writer;
	private UserDao userDao;

	public ClientSendThread(Socket s, User sender, ObjectOutputStream writer) {
		this.sender = sender;
		this.s = s;
		this.writer = writer;
		userDao = new UserDao();
		System.out.println("ClientSendThread start!");
	}

	@Override
	public void run() {
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			String line = in.nextLine();
			/**
			 * 在这里通过控制台输入的消息 通过格式创建Message
			 */
			System.out.println(line);
			if (line != null && !line.isEmpty()) {
				int uid = Integer.parseInt(line.substring(0, line.indexOf(":")));
				String message = line.substring(line.indexOf(":") + 1);
				User receiver = userDao.getUser(uid);
				Message m = new Message(sender, receiver, message, new Timestamp(System.currentTimeMillis()));
				try {
					writer.writeObject(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
