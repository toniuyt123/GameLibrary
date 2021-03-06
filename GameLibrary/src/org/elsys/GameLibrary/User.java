package org.elsys.GameLibrary;
import java.sql.*;
import java.util.Scanner;

public class User {
	private String userName;
	private String realName;
	private String passHash;
	private byte[] passSalt;
	private int age;
	private int id;
	
	public User(int id, String userName, String realName, String passHash, byte[] passSalt, int age) {
		this.id = id;
		this.userName = userName;
		this.realName = realName;
		this.passHash = passHash;
		this.passSalt = passSalt;
		this.age = age;
	}
	
	public User(String userName, String realName, String passHash, byte[] passSalt, int age) {
		this.userName = userName;
		this.realName = realName;
		this.passHash = passHash;
		this.passSalt = passSalt;
		this.age = age;
	}
	
	public String getRealName() {
		return this.realName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPassHash() {
		return this.passHash;
	}
	
	public byte[] getPassSalt() {
		return this.passSalt;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static void addUser(Connection conn, User user) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		try {
			String insertSql = "INSERT INTO Users "
					+ "(RealName, UserName, Age, PasswordHash, PasswordSalt)"
					+ "VALUES(?, ?, ?, ?, ?)";
			prp = conn.prepareStatement(insertSql);
			prp.setString(1, user.getRealName());
			prp.setString(2, user.getUserName());
			prp.setInt(3, user.getAge());
			prp.setString(4, user.getPassHash());
			prp.setBytes(5, user.getPassSalt());
			//System.out.println(user.getPassSalt());
			prp.executeUpdate();
			conn.commit();
		}catch (Exception e){
			System.out.println(e.getMessage());
			conn.rollback();
		} finally {
			conn.setAutoCommit(false);
			if (prp != null) {
				prp.close();
			}
		}
	}
	
	public static byte[] getSalt(Connection conn, String userName) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		try {
			String selectString = "SELECT PasswordSalt FROM Users WHERE UserName = ?";
			prp = conn.prepareStatement(selectString);
			prp.setString(1, userName);
			ResultSet rs = prp.executeQuery();
			rs.next();
			byte[] passsalt = rs.getBytes("PasswordSalt");
			System.out.println("GET +" + passsalt);
			return passsalt;
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		} finally {
			if (prp != null) {
				prp.close();
			}
		}
	}
	
	public static User getUser(Connection conn, String userName, String passHash) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		try {
			String selectString = "SELECT * FROM Users WHERE UserName = ? AND PasswordHash = ?";
			prp = conn.prepareStatement(selectString);
			prp.setString(1, userName);
			prp.setString(2, passHash);
			ResultSet rs = prp.executeQuery();
			rs.next();
			String realname = rs.getString("RealName");
			byte[] passsalt = rs.getBytes("PasswordSalt");
			int age = rs.getInt("Age");
			int id = rs.getInt("Id");
			return new User(id, userName, realname, passHash, passsalt, age);
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		} finally {
			if (prp != null) {
				prp.close();
			}
		}
		
	}
	public void addGame(Connection conn, Scanner in) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		conn.setAutoCommit(false);
		try {
			String insertString = "INSERT INTO GamesUsers "
								+ "(UserId, GameId, Rating, Status)"
								+ "VALUES(?, ?, ?, ?)";
			prp = conn.prepareStatement(insertString);
			prp.setInt(1, id);
			GameLibrary.showAllGames(conn);
			System.out.println("Enter game id");
			prp.setInt(2, in.nextInt());
			System.out.println("Enter game rating");
			prp.setFloat(3, in.nextFloat());
			System.out.println("Enter game Status");
			prp.setInt(4, in.nextInt());
			prp.executeUpdate();
			conn.commit();
		}catch (Exception e){
			System.out.println(e.getMessage());
			conn.rollback();
		} finally {
			if (prp != null) {
				prp.close();
			}
		}
	}
	
	public void removeGame(Connection conn, Scanner in) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		conn.setAutoCommit(false);
		try {
			String deleteString = "DELETE FROM GamesUsers "
								+ "WHERE UserId = ? AND GameId = ?";
			prp = conn.prepareStatement(deleteString);
			prp.setInt(1, id);
			GameLibrary.showUserGames(this, conn);
			System.out.println("Enter game id");
			prp.setInt(2, in.nextInt());
			prp.executeUpdate();
			conn.commit();
		}catch (Exception e){
			System.out.println(e.getMessage());
			conn.rollback();
		} finally {
			if (prp != null) {
				prp.close();
			}
		}
	}
	
	public void updateGame(Connection conn, Scanner in) throws SQLException {
		PreparedStatement prp = null;
		conn.setAutoCommit(false);
		try {
			String updateString = "UPDATE GamesUsers gu "
								+ "SET gu.Rating = ?, gu.Status = ?"
								+ "WHERE gu.UserId = ? AND gu.GameId = ?"; 
			prp = conn.prepareStatement(updateString);
			prp.setInt(3, id);
			GameLibrary.showUserGames(this, conn);
			System.out.println("Enter game id: ");
			prp.setInt(4, in.nextInt());
			System.out.println("Enter new rating: ");
			prp.setFloat(1, in.nextFloat());
			System.out.println("Enter new status: ");
			prp.setInt(2, in.nextInt());
			prp.executeUpdate();
			conn.commit();
		}catch (Exception e){
			conn.rollback();
			System.out.println(e.getMessage());
		} finally {
			if (prp != null) {
				prp.close();
			}
		}
	}
	
}
