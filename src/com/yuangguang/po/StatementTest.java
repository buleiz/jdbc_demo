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
	//Statement弊端，需要拼写sql语句，并且存在SQL注入问题
	//如何避免出现sql注入，只要PreparedStatement(从 Statement扩展而来) 取代Statement
	public void testLogin() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入用户名：");
		String username = scanner.nextLine();
		System.out.println("请输入密码");
		String password = scanner.nextLine();
		
		String sql = "SELECT username,password FROM user WHERE username = '"+username+"' AND password = '"+password+"'";
		System.out.println(sql);
		User user = get(sql, User.class);
		if(user != null) {
			System.out.println("登录成功！");
		} else {
			System.out.println("登录失，用户不存在或者密码错误！");
		}
	}
	
	//使用Stetement实现对数据库表的查询操作
	public <T> T get(String sql, Class<T> clazz) {
		T t = null;
		
		Connection  conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			// 1、加载配置文件
			InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);
			
			// 2、读取配置信息
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");
			
			//3、加载驱动
			Class.forName(driverClass);
			
			//4、获取连接
			conn = DriverManager.getConnection(url, user, password);
			
			st = conn.createStatement();
			
			rs = st.executeQuery(sql);
			
			// 获取结果对象的元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			
			// 获取结果集的列数
			int columCount = rsmd.getColumnCount();
			if(rs.next()) {
				t = clazz.newInstance();
				
				for(int i = 0; i < columCount; i++) {
					//1、获取列的名称
					//String columName = rsmd.getColumNameName(i+1);
					
					//1、获取列的别名
					String columName = rsmd.getColumnLabel(i+1);
					
					//2、根据列名获取对应数据表的数据
					Object columVal = rs.getObject(columName);
					
					//3、将数据表中得到的数据，封装进对象
					Field field = clazz.getDeclaredField(columName);
					field.setAccessible(true);
					field.set(t, columVal);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
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
