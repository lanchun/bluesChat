package wang.lanchun.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import wang.lanchun.client.ClientManager;
import wang.lanchun.dao.UserDao;
import wang.lanchun.entity.Message;
import wang.lanchun.entity.User;
import wang.lanchun.utils.ImgCompress;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	// 接收者id
	private User receiver;
	private JTextArea msgShowTA;
	private UserDao userDao;
	private long lastTime;
	// 第一次发送消息的标志
	private int flag = 1;
	private JScrollPane scrollPane_1;
	private JTextPane textPane;
	private JButton btnImage;

	/**
	 * Create the frame.
	 */
	public ChatFrame(final int id) {
		this.lastTime = System.currentTimeMillis();
		this.userDao = new UserDao();
		this.receiver = userDao.getUser(id);

		// 关闭聊天窗口监听事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatFrame.this.dispose();
				ClientManager.frames.remove(id);
			}
		});

		setBounds(100, 100, 450, 339);
		setTitle(receiver.getNickname());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();

		JButton button = new JButton("\u53D1\u9001");

		// 发送消息监听器
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String msg = textPane.getText();
				appendToMessage(msg, System.currentTimeMillis());
				textPane.setText("");
				sendMessage(msg);
			}
		});

		scrollPane_1 = new JScrollPane();

		btnImage = new JButton("\u63D2\u5165\u56FE\u7247");
		btnImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileFilter(new ImageFilter());
				jfc.showDialog(new JLabel(), "选择图片");
				File file = jfc.getSelectedFile();

				System.out.println("文件:" + file.getAbsolutePath());
				System.out.println(jfc.getSelectedFile().getName());

				// 压缩图片
				ImgCompress imgCom = new ImgCompress(file.getAbsolutePath());
				imgCom.resizeFix(ImgCompress.WIDTH, ImgCompress.HEIGHT);
				textPane.insertIcon(new ImageIcon(imgCom.resizeFix(ImgCompress.WIDTH, ImgCompress.HEIGHT)));
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(268).addComponent(btnImage)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(button)));
		gl_contentPane
				.setVerticalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_contentPane.createSequentialGroup()
												.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 187,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 72,
														GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(button)
										.addComponent(btnImage))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		textPane = new JTextPane();
		scrollPane_1.setViewportView(textPane);

		msgShowTA = new JTextArea();
		msgShowTA.setLineWrap(true);// 激活自动换行功能
		msgShowTA.setWrapStyleWord(true);// 激活断行不断字功能
		scrollPane.setViewportView(msgShowTA);
		contentPane.setLayout(gl_contentPane);
	}

	// 打印时间
	public void appendTime(long time) {
		if (flag == 1 || (time - lastTime >= 60000)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			msgShowTA.append(format.format(new Date(time)) + "\n");
			lastTime = time;
			flag = 0;
		}
	}

	// 别人发给我的消息
	public void appendComingMessage(Message m) {
		appendTime(m.getTime().getTime());
		// msgShowTA.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		msgShowTA.append(m.getContent() + "\n");
	}

	// 我发给别人的消息
	public void appendToMessage(String msg, long time) {
		appendTime(time);
		// msgShowTA.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		msgShowTA.append(msg + "\n");
	}

	// 从消息记录中取出的消息
	public void appendFromRecord(Message m) {
		if (m.getSender().getId() == ClientManager.user.getId()) {
			appendToMessage(m.getContent(), m.getTime().getTime());
		} else {
			appendComingMessage(m);
		}
	}

	public void sendMessage(String msg) {
		System.out.println(msg);
		if (msg != null && !msg.isEmpty()) {
			Message m = new Message(ClientManager.user, receiver, msg, new Timestamp(System.currentTimeMillis()));
			try {
				ClientManager.writer.writeObject(m);
				ClientManager.writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
