package wang.lanchun.view;

import java.awt.Font;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import wang.lanchun.client.ClientManager;
import wang.lanchun.dao.UserDao;
import wang.lanchun.entity.User;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatFriendBoard extends JFrame {

	private JPanel contentPane;
	private UserDao userDao;
	private JLabel lbUsername;
	private JLabel lbUsericon;
	public static JList<User> list;
	public static User currentUser;
	public static ClientManager clientManager;
	public static DefaultListModel<User> model = new DefaultListModel<>();

	/**
	 * Create the frame.
	 */
	public ChatFriendBoard(final User user) {
		addWindowListener(new WindowAdapter() {
			//关闭程序监听事件
			@Override
			public void windowClosing(WindowEvent e) {
				userDao.switchOnline(user.getId(), 0);
				/**
				 * 将未读消息保存到文件,注意：这里的消息都是未读的，下一次打开应该还是未读
				 * 未读消息和消息记录分开保存
				 */
				
				
			}
		});
		
		
		if (user == null) {
			JOptionPane.showMessageDialog(ChatFriendBoard.this, "程序错误！");
			throw new RuntimeException("用户为空");
		}
		System.out.println(user);
		this.currentUser = user;
		userDao = new UserDao();
		clientManager = ClientManager.getInstance();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 256, 519);
		setTitle("BluesChat");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		lbUsername = new JLabel(currentUser.getNickname());
		setContentPane(contentPane);

		JPanel panel = new JPanel();

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, 230,
												GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)));

		lbUsername.setFont(new Font("微软雅黑", Font.PLAIN, 15));

		lbUsericon = new JLabel("New label");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(lbUsericon, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
						.addComponent(lbUsername, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lbUsericon, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
								.addComponent(lbUsername, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
				.addContainerGap()));
		panel.setLayout(gl_panel);

		/**
		 * 获取好友列表信息 重开一个线程，从数据库中取数据
		 */

		list = new JList<>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JList<User> source = (JList<User>) e.getSource();
					User selected = (User) source.getSelectedValue();
					System.out.println(selected.getId());

					clientManager.launchChatFrame(selected.getId());
				}
			}
		});

		// 获取好友列表
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				loadFriendsInfo();
			}

		});

		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
	}

	private void loadFriendsInfo() {
		List<User> users = userDao.getAllUsers();
		for(User u : users){
			model.addElement(u);
		}
		list.setModel(model);
		ClientManager.updateBoard();
	}

	/*
	 * private void loadFriendsInfo() { final List<User> users =
	 * userDao.getAllUsers(); if (users != null) { list.setModel(new
	 * AbstractListModel<User>() {
	 * 
	 * private static final long serialVersionUID = 1L;
	 * 
	 * @Override public int getSize() { return users.size(); }
	 * 
	 * @Override public User getElementAt(int index) { return users.get(index);
	 * } }); } }
	 */

}
