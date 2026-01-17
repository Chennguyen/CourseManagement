/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class CartDAO {
    // 1. LẤY GIỎ HÀNG TỪ DB LÊN (Dùng khi Login)
    public Cart getCartByUserID(String userID) {
        Cart cart = new Cart();
        Map<String, CourseDTO> items = new HashMap<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            // Join bảng Cart với bảng Course để lấy đủ thông tin (Tên, Giá...)
            String sql = "SELECT C.id, C.name, C.description, C.duration, C.fee, C.lecturer, C.roadmap " +
                         "FROM tblCart T JOIN tblCourses C ON T.courseID = C.id " +
                         "WHERE T.userID = ?";
            ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            rs = ptm.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                float fee = rs.getFloat("fee");
                String lecturer = rs.getString("lecturer");
                String roadmap = rs.getString("roadmap");
                
                // Tạo DTO và đưa vào Map
                CourseDTO dto = new CourseDTO(id, name, description, duration, fee, lecturer, roadmap);
                items.put(id, dto);
            }
            cart.setCart(items); // Set map vào Cart
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(ptm!=null) ptm.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return cart;
    }

    // 2. THÊM VÀO DB (Dùng khi Add to Cart)
    public void addToCart(String userID, String courseID) {
        try {
            Connection conn = DBUtils.getConnection();
            // Check xem đã có chưa, nếu chưa mới insert (Tránh duplicate)
            String sqlCheck = "SELECT cartID FROM tblCart WHERE userID=? AND courseID=?";
            PreparedStatement ptm = conn.prepareStatement(sqlCheck);
            ptm.setString(1, userID);
            ptm.setString(2, courseID);
            ResultSet rs = ptm.executeQuery();
            if (!rs.next()) {
                // Chưa có thì Insert
                String sqlInsert = "INSERT INTO tblCart(userID, courseID, quantity) VALUES(?, ?, 1)";
                PreparedStatement ptm2 = conn.prepareStatement(sqlInsert);
                ptm2.setString(1, userID);
                ptm2.setString(2, courseID);
                ptm2.executeUpdate();
                ptm2.close();
            }
            rs.close();
            ptm.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. XÓA KHỎI DB (Dùng khi Remove)
    public void removeFromCart(String userID, String courseID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM tblCart WHERE userID=? AND courseID=?";
            PreparedStatement ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            ptm.setString(2, courseID);
            ptm.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. XÓA SẠCH DB (Dùng khi Checkout xong)
    public void clearCart(String userID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM tblCart WHERE userID=?";
            PreparedStatement ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            ptm.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
