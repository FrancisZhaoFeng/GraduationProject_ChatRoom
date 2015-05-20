package com.csu.client;

import java.awt.TextField;

import javax.swing.*;

public class test extends JFrame{
	
	private JPanel panel;
	private JButton btn;
	private TextField portField;
	private JLabel label;
	private int flag = 0; 	//启动标识
//	private ChatServer server = null;
	public test(){
		panel = new JPanel();
		label = new JLabel();
		label.setText("请输入端口号:");
		btn = new JButton();
		btn.setText("启动服务器");
		portField = new TextField(30);
//		btn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				String port = portField.getText().trim();
//				try {
//					if(flag == 0){
//						server = new ChatServer(Integer.parseInt(port));
//						flag = 1;
//						modifyTitle();
//						server.start();
//					}else{
//						if(null!= server){
//							server.quit();
//							flag = 0;
//							modifyTitle();
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
		panel.add(label);
		panel.add(portField);
		panel.add(btn);
		getContentPane().add(panel);
		this.setSize(500,400);
		this.setLocation(400, 200);
		this.setTitle("服务器未启动");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void modifyTitle(){
		if(flag == 0){
			this.setTitle("服务器未启动");
			btn.setText("启动服务器");
		}else{
			this.setTitle("服务器已启动");
			btn.setText("关闭服务器");
			this.repaint();
		}
		this.repaint();
	}
	public static void main(String[] args) {
		test client = new test();
	}
}
