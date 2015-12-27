package wang.lanchun.view;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import wang.lanchun.client.ClientManager;
import wang.lanchun.dao.UserDao;
import wang.lanchun.entity.User;

import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tFUsername;
	private JTextField tFPasswd;
	private UserDao userDao;

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		userDao = new UserDao();
		
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 204);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
		
		JLabel label_1 = new JLabel("\u5BC6  \u7801\uFF1A");
		
		tFUsername = new JTextField();
		tFUsername.setColumns(10);
		
		tFPasswd = new JTextField();
		tFPasswd.setColumns(10);
		
		JButton btnLogin = new JButton("\u767B\u5F55");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//1.先检查用户数据是否合法
				String username = tFUsername.getText();
				String passwd = tFPasswd.getText();
				if(username.isEmpty()){
					JOptionPane.showMessageDialog(LoginFrame.this, "用户名不能为空！");
					return;
				}
				if(passwd.isEmpty()){
					JOptionPane.showMessageDialog(LoginFrame.this, "密码不能为空！");
					return;
				}
				User user = userDao.login(username, passwd);
				String msg;
				if(user == null){
					msg = "用户名或密码不正确！";
					JOptionPane.showMessageDialog(LoginFrame.this, msg);
					return;
				}
				
				//2.登录消息服务器
				ClientManager.user = user;
					ClientManager cm = ClientManager.getInstance();
					cm.loginToChatServer();
				LoginFrame.this.dispose();
				launchFriendBoard(user);
			}
		});
		
		JButton btReg = new JButton("\u6CE8\u518C");
		btReg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				launchRegFrame();
			}

			private void launchRegFrame() {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							RegForm frame = new RegForm();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btReg, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label_1)
							.addGap(18)
							.addComponent(tFPasswd, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label)
							.addGap(18)
							.addComponent(tFUsername, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)))
					.addContainerGap(43, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(tFUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(tFPasswd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btReg))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		setTitle("用户登录");
	}
	
	private void launchFriendBoard(final User user){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFriendBoard frame = new ChatFriendBoard(user);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
