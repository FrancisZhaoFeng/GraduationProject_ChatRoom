package com.csu.client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
	private JPanel pan1;
	private JPanel pan2;
	private JButton btn1;
	private JButton btn2;
//	private JLabel jlAccount;
//	private JLabel jlPwd;
//	private JTextField jfAccount;
//	private JTextField jfPwd;
//	private JButton jbLogin;
//	private JButton jbcancel;
	
	

	public LoginWindow() {
		pan1 = new JPanel();
		pan2 = new JPanel();
		btn1 = new JButton("跳转到2");
		btn2 = new JButton("跳转到1");
		pan1.add(btn1);
		pan2.add(btn2);
		
		this.setBounds(400, 200,500,400);
		this.setTitle("登陆界面");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(pan1);
		this.setVisible(true);
		while(true){
			btn1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
//	private void test(){
//		boolean result = true;
//		while(true){
//			
//		}
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoginWindow lg = new LoginWindow();
	}

}
