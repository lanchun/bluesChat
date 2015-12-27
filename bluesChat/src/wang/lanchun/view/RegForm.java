package wang.lanchun.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import wang.lanchun.dao.UserDao;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegForm extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private UserDao userDao;

	/**
	 * Create the frame.
	 */
	public RegForm() {
		
		userDao = new UserDao();
		
		setTitle("\u7528\u6237\u6CE8\u518C");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblUsername = new JLabel("username:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("password:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("reg");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String username = textField.getText();
				String password = textField_1.getText();
				if(username.isEmpty()){
					JOptionPane.showMessageDialog(RegForm.this, "用户名不能为空！");
					return;
				}
				if(password.isEmpty()){
					JOptionPane.showMessageDialog(RegForm.this, "密码不能为空！");
					return;
				}
				boolean b = userDao.reg(username, password);
				if(b){
					JOptionPane.showMessageDialog(RegForm.this, "注册成功！");
					RegForm.this.dispose();
				}else{
					JOptionPane.showMessageDialog(RegForm.this, "注册失败！");
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(54)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblPassword)
									.addGap(18)
									.addComponent(textField_1))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblUsername)
									.addGap(18)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(87)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(149, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(42)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(48)
					.addComponent(btnNewButton)
					.addContainerGap(59, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
