package com.usermanagement.dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.usermanagement.model.User;

import java.sql.Connection;

//This DAO class provides CRUD database operations for the table users in the database
public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/crud?useSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword = "12345678";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name,email,country) VALUES "
	+"(?,?,?)";
	
	private static final String SELECT_USER_BY_ID = "SELECT id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "SELECT * from users";
	private static final String UPDATE_USERS_SQL = "UPDATE users set name=?,email=?,country=? where id = ?";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?";
	
	protected Connection getConnection(){
		Connection connection = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}catch(SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	//create or insert user
	public void insertUser(User user) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(INSERT_USERS_SQL)){
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getCountry());
			pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//update user
	public boolean updateUser(User user)throws SQLException{
		boolean rowUpdated=false;
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(UPDATE_USERS_SQL)){
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getCountry());
			pstmt.setInt(4, user.getId());
			rowUpdated = pstmt.executeUpdate()>0;
		}catch(Exception e){
			e.printStackTrace();
		}
		return rowUpdated;
	}
	//select user by id
	public User selectUser(int id)throws SQLException{
		User user=null;
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_ID)){
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	//select users
	public List<User> selectAllUser(){
		List<User> users = new ArrayList<>();
		try(Connection conn = getConnection();
				Statement statement = conn.createStatement()){
			ResultSet rs = statement.executeQuery(SELECT_ALL_USERS);
			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id,name,email,country));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return users;
	}
	//delete user
	public boolean deleteUser(int id)throws SQLException{
		boolean rowDeleted=false;
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(DELETE_USERS_SQL)){
			pstmt.setInt(1, id);
			rowDeleted = pstmt.executeUpdate()>0;
		}catch(Exception e){
			e.printStackTrace();
		}
		return rowDeleted;
	}
	
}
