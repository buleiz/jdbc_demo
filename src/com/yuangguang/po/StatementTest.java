package com.yuangguang.po;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Test;

public class StatementTest {
	@Test
	//Statement�׶ˣ���Ҫƴдsql��䣬���Ҵ���SQLע������
	//��α������sqlע�룬ֻҪPreparedStatement(�� Statement��չ����) ȡ��Statement
	public void testLogin() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("�������û�����");
		String username = scanner.nextLine();
		System.out.println("����������");
		String password = scanner.nextLine();
		
		String sql = "SELECT username,password FROM user WHERE username = '"+username+"' AND password = '"+password+"'";
		System.out.println(sql);
		User user = get(sql, User.class);
		if(user != null) {
			System.out.println("��¼�ɹ���");
		} else {
			System.out.println("��¼ʧ���û������ڻ����������");
		}
	}
	
	//ʹ��Stetementʵ�ֶ����ݿ��Ĳ�ѯ����
	public <T> T get(String sql, Class<T> clazz) {
		T t = null;
		
		Connection  conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			// 1�����������ļ�
			InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);
			
			// 2����ȡ������Ϣ
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");
			
			//3����������
			Class.forName(driverClass);
			
			//4����ȡ����
			conn = DriverManager.getConnection(url, user, password);
			
			st = conn.createStatement();
			
			rs = st.executeQuery(sql);
			
			// ��ȡ��������Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			
			// ��ȡ�����������
			int columCount = rsmd.getColumnCount();
			if(rs.next()) {
				t = clazz.newInstance();
				
				for(int i = 0; i < columCount; i++) {
					//1����ȡ�е�����
					//String columName = rsmd.getColumNameName(i+1);
					
					//1����ȡ�еı���
					String columName = rsmd.getColumnLabel(i+1);
					
					//2������������ȡ��Ӧ���ݱ������
					Object columVal = rs.getObject(columName);
					
					//3�������ݱ��еõ������ݣ���װ������
					Field field = clazz.getDeclaredField(columName);
					field.setAccessible(true);
					field.set(t, columVal);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ر���Դ
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
