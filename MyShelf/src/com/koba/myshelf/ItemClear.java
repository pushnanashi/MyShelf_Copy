package com.koba.myshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemClear extends Thread {

	
	Pass passes = new Pass();
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	
	
	String table =  null;
	int item  = 0;
	
	ItemClear(String t,int i){
		
		table = t;
		item = i;
		
	}
	

		
 public void run() {
		Connection con = null;
	    PreparedStatement ps = null;

	    try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

         // データベースへ接続
	    try {
             con = DriverManager.getConnection(PATH,USER,DBPASS);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        java.sql.Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}

         String ex = "delete from "+table +" where id = " + item;

         System.out.println(ex);

       
         try {
			int result=stmt.executeUpdate(ex);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		

		   }
	
	
	
	
	
	
}
