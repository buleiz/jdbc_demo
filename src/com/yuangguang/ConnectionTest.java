package com.yuangguang;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;


public class ConnectionTest {
	
	@Test
	//��ʽһ
	public void testConnection1() throws SQLException{
		//��ȡDriverʵ�������
		Driver driver = new com.mysql.jdbc.Driver();
		
		//�������ݿ�����·��
		//jdbc:mysql Э��:��Э��
		//localhost��ip��ַ
		//3306��Ĭ�϶˿ں�
		//test�����ݿ�
		String url = "jdbc:mysql://localhost:3306/test";
		
		//���û����������װ��Properties��
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		//���ݿ����Ӷ���
		Connection conn = driver.connect(url, info);
		
		System.out.println(conn);
	}
	
	//��ʽ�� �÷���ķ�ʽʵ��Driverʵ������󣬲����ֵ�����API
	@Test
	public void testConnection2() throws Exception{
		//1����ȡDriverʵ�������
		Class clazz = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//2���ṩҪ���ӵ����ݿ�
		String url = "jdbc:mysql://localhost:3306/test";
		
		//3���ṩ������Ҫ���û���������
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		//���ݿ����Ӷ���
		Connection conn = driver.connect(url, info);
		
		System.out.println(conn);
	}
	
	//��ʽ�� ʹ��DriverManager�滻Driver
	@Test
	public void testConnection3() throws Exception{
		//1����ȡDriverʵ�������
		Class clazz = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//2���ṩҪ���ӵ����ݿ�
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		
		//ע������
		DriverManager.registerDriver(driver);
		
		//��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	
	//��ʽ�� ����ֻ��˵����������������ʾ��ע����������
	@Test
	public void testConnection4() throws Exception{
		//1���ṩҪ���ӵ����ݿ�
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		
		//2������Driver
		Class.forName("com.mysql.jdbc.Driver");
		//����ڷ�ʽ������ʡ�����в���
//		Driver driver = (Driver) clazz.newInstance();
//		//ע������
//		DriverManager.registerDriver(driver);
		
		//3����ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	
	//��ʽ�壨final�棩 �����ݿ�������Ҫ�ļ���������Ϣ�����������ļ��У�ͨ����ȡ�����ļ��ķ�ʽ���������
	/**
	 * ���ַ�ʽ�ĺô���
	 * 1��ʵ�������������ķ��룬ʵ���˽���
	 * 2�������Ҫ�޸�������Ϣ���Ϳ��Ա���������´��ʱ��
	 */
	@Test
	public void getConnection5() throws Exception {
		//1����ȡ�����ļ��µ�4��������Ϣ
		InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		
		Properties pros = new Properties();
		pros.load(is);
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		//2����������
		Class.forName(driverClass);
		
		//3����ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
}
