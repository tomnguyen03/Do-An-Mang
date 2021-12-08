/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minh_tien.doan_mang_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom
 */
public class ThreadSocketServer extends Thread {
    Socket socket = null;
    DataInputStream din = null;
    DataOutputStream dos = null;
    String text="";
    private Connection con;
    private Statement stm;
    private ResultSet rs;
    private String sql;
    public ThreadSocketServer(Socket socket) {

        System.out.println("Call to thread socket. ");
        System.out.println("Socket is connected: " + socket.isConnected());
        System.out.println("Socket address: " + socket.getInetAddress().getHostAddress());
        System.out.println("Socket port: " + socket.getPort());
        this.socket = socket;
    }
    public void run() {
        String result="";
        try {
            din = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            while(true) {
                text = din.readUTF();
                String[] data = text.split("\n");
                if (data[0].equals("GV")) {
                    result = Profile(data[1]);
                    dos.writeUTF(result);
                    dos.flush();
                    text="";
                }
                if (data[0].equals("btnView")) {
                    result = DiemThi(data[1]);
                    dos.writeUTF(result);
                    dos.flush();
                    text="";
                }
                if (data[0].equals("btnAdd")) {
                    result = TenSV(data[1])+MonHoc(data[1]);
                    dos.writeUTF(result);
                    dos.flush();
                    text="";
                }
                if (data[0].equals("BtnSubmit")) {
                    result = AddScore(data[1], data[2], data[3], data[4], data[5]);
                    dos.writeUTF(result);
                    dos.flush();
                    text="";
                }
                else {
                    String user = data[0];
                    String pass = data[1];
                    if (!user.equals("") && !pass.equals("")) {
                        if (CheckAccount(user, pass).equals("failed")) {
                            result = "failed";
                        }
                        else {
                            String role = CheckAccount(user, pass);
                            if (role.equals("SV")) {
                                result = role+"\n"+Profile(user)+DiemThi(user);
                            }
                            else result = role;
                        }
                        System.out.println(result);
                        dos.writeUTF(result);
                        dos.flush();
                        text="";
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(ThreadSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String AddScore(String mssv, String monHoc, String CC, String GK, String CK) throws Exception {
        ConnectData();
        String sql = "UPDATE diemthi SET ChuyenCan = "+CC+",GiuaKi = "+GK+",CuoiKi = "+CK+" WHERE MaSV='"+mssv+"' AND MonHoc='"+monHoc+"' ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        int rowAffected = pstmt.executeUpdate();
        System.out.println(sql+".."+rowAffected);
        if (rowAffected==1) return "OK";
        return "failed";
    }
    public String Profile(String mssv) throws Exception {
        ConnectData();
        String sql = "SELECT * FROM `profile` WHERE `MaSV` LIKE '"+mssv+"'";
        rs = stm.executeQuery(sql);
        String result="a";
        if (rs.next()) {
            result =rs.getString(2)+"\n"+rs.getString(3)+"\n"+rs.getString(4)+"\n"+rs.getString(5)+"\n"+rs.getString(6)+"\n"+rs.getString(7)+"\n";
        }
                
        con.close();
        return result;
    }
    public String MonHoc(String mssv) throws Exception {
        ConnectData();
        String sql = "SELECT * FROM `diemthi` WHERE `MaSV` LIKE '"+mssv+"'";
        rs = stm.executeQuery(sql);
        String monHoc="";
        while (rs.next()) {
            monHoc = monHoc + rs.getString("MonHoc")+"\t";
        }
        con.close();
        return monHoc;
    }
    public String TenSV(String mssv) throws Exception {
        ConnectData();
        String sql = "SELECT * FROM `profile` WHERE `MaSV` LIKE '"+mssv+"'";
        rs = stm.executeQuery(sql);
        String tenSV="";
        while (rs.next()) {
            tenSV = rs.getString("HoTen")+"\n";
        }
        con.close();
        return tenSV;
    }
    public String DiemThi(String mssv) throws Exception {
        ConnectData();
        String sql = "SELECT * FROM `diemthi` WHERE `MaSV` LIKE '"+mssv+"'";
        rs = stm.executeQuery(sql);
        String MonHoc = "";
        String ChuyenCan = "";
        String GiuaKi = "";
        String CuoiKi = "";
        String result = "";
        while (rs.next()) {
//            result =rs.getString(2)+"\n"+rs.getString(3)+"\n"+rs.getString(4)+"\n"+rs.getString(5)+"\n"+rs.getString(6)+"\n";
              MonHoc = MonHoc+rs.getString("MonHoc")+"\n";
              ChuyenCan = ChuyenCan + rs.getString("ChuyenCan")+"\n";
              GiuaKi = GiuaKi + rs.getString("GiuaKi")+"\n";
              CuoiKi = CuoiKi + rs.getString("CuoiKi")+"\n";
        }
        result = MonHoc+ChuyenCan+GiuaKi+CuoiKi;
        con.close();
        return result;
    }
    public String CheckAccount(String user, String pass) throws Exception {
        ConnectData();
        String sql = "SELECT * FROM `account` WHERE `user` LIKE '"+user+"' AND `pass` LIKE '"+pass+"'";
        rs = stm.executeQuery(sql);
        if (rs.next()) {
//            if(rs.getString("role").equals("")) return "failed";
//            else return rs.getString("role");
            return rs.getString("role");
        }
        return "failed";
    }
    public void ConnectData() throws Exception {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String URL = "jdbc:mysql://localhost:3306/xemdiemsv";
                String user = "root";
                String Password = "";
                con = DriverManager.getConnection(URL, user, Password);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connect failure");
                e.printStackTrace();
            }
            stm = con.createStatement();
        } catch (Exception e) {
            System.out.println("khong nap duoc driver" + e);
        }
    }
}
