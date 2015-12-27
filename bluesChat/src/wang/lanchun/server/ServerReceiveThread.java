package wang.lanchun.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import wang.lanchun.dao.MessageDao;
import wang.lanchun.entity.Message;

/**
 * 客户处理线程
 * 
 * @author lanchun
 *
 */
public class ServerReceiveThread implements Runnable {

	private ObjectInputStream reader;
	private Socket s;
	private MessageDao messageDao;

	public ServerReceiveThread(Socket s, ObjectInputStream reader) {
		this.s = s;
		this.reader = reader;
		this.messageDao = new MessageDao();
		System.out.println("ServerReceiveThread started!");
	}

	@Override
	public void run() {
		try {
			while (true) {
				// 这里的socket出了问题
				if (s == null) {
					System.out.println("socket is null");
					throw new RuntimeException("socket异常");
				}
				if (s.isClosed()) {
					System.out.println("socket is closed");
					throw new RuntimeException("socket异常");
				}
				Message msg = (Message) reader.readObject();
				if (msg != null) {
					int id = msg.getReceiver().getId();
					// 如果在线，立即转发消息
					System.out.println("got message!" + msg.getContent() + "," + msg.getReceiver().isOnline());
					if (msg.getReceiver().isOnline()) {

						// 取得接收者的socket
						Socket receiverSo = Server.clients.get(msg.getReceiver().getId());

						// 这里先去保存着所有的输出流的map中去找，如果有则直接取出来，没有则创建并保存到map中
						ObjectOutputStream writer = Server.writers.get(receiverSo);
						if (writer == null) {
							writer = new ObjectOutputStream(receiverSo.getOutputStream());
							Server.writers.put(receiverSo, writer);
						}

						writer.writeObject(msg);
					} else {
						// 存入离线消息数据库
						msg.setRead(false);
						messageDao.add(msg);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
