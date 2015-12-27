package wang.lanchun.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wang.lanchun.entity.User;

public class Server {
	public static ServerSocket ss;
	public static ExecutorService exec;
	public static Map<Integer, Socket> clients;
	public static Map<Socket,ObjectOutputStream> writers;

	public static void main(String[] args) throws ClassNotFoundException {
		try {
			ss = new ServerSocket(10086);
			exec = Executors.newCachedThreadPool();
			clients = new HashMap<>();
			writers = new HashMap<>();
			
			System.out.println("server start ...");

			while (true) {
				/**
				 *  每当有客户端连接进来，先确定客户端身份，再创建新线程处理请求
				 */
				Socket s = ss.accept();
				ObjectInputStream reader = new ObjectInputStream(s.getInputStream());
				User u = (User) reader.readObject();
				int uid = u.getId();
				clients.put(uid, s);
				System.out.println(uid+" logined");
				
				exec.execute(new ServerReceiveThread(s,reader));
			}
			// reader = new BufferedReader(new
			// InputStreamReader(s.getInputStream(), Charset.forName("UTF-8")));
			// writer = new PrintWriter(new
			// OutputStreamWriter(s.getOutputStream(),
			// Charset.forName("UTF-8")));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
