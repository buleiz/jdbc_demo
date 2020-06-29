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
	//方式一
	public void testConnection1() throws SQLException{
		//获取Driver实现类对象
		Driver driver = new com.mysql.jdbc.Driver();
		
		//定义数据库连接路径
		//jdbc:mysql 协议:子协议
		//localhost：ip地址
		//3306：默认端口号
		//test：数据库
		String url = "jdbc:mysql://localhost:3306/test";
		
		//将用户名和密码封装在Properties中
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		//数据库连接对象
		Connection conn = driver.connect(url, info);
		
		System.out.println(conn);
	}
	
	//方式二 用反射的方式实现Driver实现类对象，不出现第三方API
	@Test
	public void testConnection2() throws Exception{
		//1、获取Driver实现类对象
		Class clazz = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//2、提供要连接的数据库
		String url = "jdbc:mysql://localhost:3306/test";
		
		//3、提供连接需要的用户名和密码
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		//数据库连接对象
		Connection conn = driver.connect(url, info);
		
		System.out.println(conn);
	}
	
	//方式三 使用DriverManager替换Driver
	@Test
	public void testConnection3() throws Exception{
		//1、获取Driver实现类对象
		Class clazz = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//2、提供要连接的数据库
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		
		//注册驱动
		DriverManager.registerDriver(driver);
		
		//获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	
	//方式四 可以只是说加载驱动，不用显示的注册驱动过了
	@Test
	public void testConnection4() throws Exception{
		//1、提供要连接的数据库
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		
		//2、加载Driver
		Class.forName("com.mysql.jdbc.Driver");
		//相较于方式三可以省略下列操作
//		Driver driver = (Driver) clazz.newInstance();
//		//注册驱动
//		DriverManager.registerDriver(driver);
		
		//3、获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	
	//方式五（final版） 将数据库连接需要的几个基本信息声明在配置文件中，通过读取配置文件的方式，获得连接
	/**
	 * 这种方式的好处？
	 * 1、实现了数据与代码的分离，实现了解耦
	 * 2、如果需要修改配置信息，就可以避免程序重新打包时间
	 */
	@Test
	public void getConnection5() throws Exception {
		//1、读取配置文件下的4个基本信息
		InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		
		Properties pros = new Properties();
		pros.load(is);
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		//2、加载驱动
		Class.forName(driverClass);
		
		//3、获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
}
