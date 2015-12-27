package wang.lanchun.client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ListModel;

import wang.lanchun.dao.UserDao;
import wang.lanchun.entity.Message;
import wang.lanchun.entity.User;
import wang.lanchun.view.ChatFrame;
import wang.lanchun.view.ChatFriendBoard;

/**
 * client自身管理类
 * 
 * @author lanchun
 *
 */

public class ClientManager {
	private static ClientManager instance;
	public static Socket s;
	public static ObjectOutputStream writer;
	private static ObjectInputStream reader;
	private static UserDao userDao;
	public static User user;

	public static Map<Integer, List<Message>> unReadMsgMap = new HashMap<>();

	// 管理所有打开的面板
	public static Map<Integer, ChatFrame> frames = new HashMap<>();

	private ClientManager() {
		userDao = new UserDao();
		try {
			s = new Socket("localhost", 10086);
			writer = new ObjectOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 单例模式
	public static ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	public void launchChatFrame(final int id) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFrame frame = new ChatFrame(id);
					frame.setVisible(true);
					frames.put(id, frame);

					if (unReadMsgMap.get(id) != null) {
						// 将未读消息list中的对应的未读消息取出来，显示在frame中
						for (Message m : unReadMsgMap.get(id)) {
							frame.appendFromRecord(m);
						}
						// 删除map中已经拿出的数据
						unReadMsgMap.remove(id);
						// 更新面板
						updateBoard(id);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	// 登录到消息服务器
	public void loginToChatServer() {
		ExecutorService exec = null;

		// reader = new ObjectInputStream(s.getInputStream());

		userDao.switchOnline(user.getId(), 1);
		try {
			writer.writeObject(user);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		exec = Executors.newFixedThreadPool(2);
		exec.execute(new ClientReceiveThread(s, user));
		// exec.execute(new ClientSendThread(s,user,writer));
	}

	// 在好友列表中显示未读消息数
	public static void updateBoard() {
		if (unReadMsgMap != null && unReadMsgMap.size() > 0) {
			for (Map.Entry<Integer, List<Message>> en : unReadMsgMap.entrySet()) {
				int id = en.getKey();
				for (int i = 0; i < ChatFriendBoard.model.size(); i++) {
					User user = ChatFriendBoard.model.getElementAt(i);
					if (user.getId() == id) {
						user.setNickname(user.getNickname() + "(" + en.getValue().size() + ")");
					}
				}
			}
		} else {
			System.out.println("unReadMsgMap 为空");
		}
	}

	// 更新面板，双击后删除未读消息数
	public static void updateBoard(int id) {
		for (int i = 0; i < ChatFriendBoard.model.size(); i++) {
			User user = ChatFriendBoard.model.getElementAt(i);
			if (user.getId() == id) {
				String nickname = user.getNickname();
				if (nickname.lastIndexOf("(") != -1) {
					user.setNickname(nickname.substring(0, nickname.lastIndexOf("(")));
				}
			}
		}
	}

	public static void updateBoard2(int id) {
		for (int i = 0; i < ChatFriendBoard.model.size(); i++) {
			User user = ChatFriendBoard.model.getElementAt(i);
			if (user.getId() == id) {
				String nickname = user.getNickname();
				String newName;
				// 如果后面有括号了，那么把未读消息数加1
				if (nickname.indexOf("(") != -1) {
					int n = Integer.parseInt(nickname.substring(nickname.indexOf("(") + 1, nickname.indexOf(")"))) + 1;
					newName = nickname.substring(0, nickname.indexOf("(") + 1) + n + ")";
				} else {
					newName = user.getNickname() + "(1)";
				}
				user.setNickname(newName);
			}
			// ChatFriendBoard.list.updateUI();
		}
	}

	// 增加未读消息到map中，当接收到消息并且聊天窗口没有打开时调用
	public static void addMap(Message m) {
		int id = m.getSender().getId();
		List<Message> temp = null;
		temp = unReadMsgMap.get(id);
		if (temp == null) {
			temp = new ArrayList<>();
			unReadMsgMap.put(id, temp);
		}
		temp.add(m);
	}

}
