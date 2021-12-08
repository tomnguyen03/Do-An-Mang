/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minh_tien.doan_mang_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Tom
 */
public class TestDB {
    private static String DB_URL = "jdbc:mysql://localhost:3306/xemdiemsv";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";
    public static void main(String[] args) {
        try {
            // connnect to database 'testdb'
            Connection conn = getConnection(DB_URL,USER_NAME,PASSWORD);
            Statement st = (Statement) conn.createStatement();
            ResultSet rs;
            String sql = "select * from profile";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1)+" "+rs.getString(2));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public static Connection getConnection(String dbURL, String userName, 
            String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, userName, password);
            System.out.println("connect successfully!");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }
}
