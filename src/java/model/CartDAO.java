/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class CartDAO {
    // 1. Sửa hàm getCartByUserID để trả về CartItem
    public Cart getCartByUserID(String userID) {
        Cart cart = new Cart();
        Map<String, CartItem> items = new HashMap<>(); // Sửa thành CartItem
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            // Lấy thêm cột quantity từ bảng tblCart
            String sql = "SELECT C.id, C.name, C.description, C.duration, C.fee, C.lecturer, C.roadmap, T.quantity " +
                         "FROM tblCart T JOIN tblCourses C ON T.courseID = C.id " +
                         "WHERE T.userID = ?";
            ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            rs = ptm.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                // ... lấy các trường khác ...
                String name = rs.getString("name");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                float fee = rs.getFloat("fee");
                String lecturer = rs.getString("lecturer");
                String roadmap = rs.getString("roadmap");
                int quantity = rs.getInt("quantity"); // Lấy quantity

                CourseDTO dto = new CourseDTO(id, name, description, duration, fee, lecturer, roadmap);
                // Tạo CartItem
                items.put(id, new CartItem(dto, quantity));
            }
            cart.setCart(items);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(ptm!=null) ptm.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return cart;
    }

    // 2. Sửa hàm addToCart để nhận quantity và xử lý logic
    public void addToCart(String userID, String courseID, int quantity) {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            // Check xem đã có chưa
            String sqlCheck = "SELECT quantity FROM tblCart WHERE userID=? AND courseID=?";
            ptm = conn.prepareStatement(sqlCheck);
            ptm.setString(1, userID);
            ptm.setString(2, courseID);
            rs = ptm.executeQuery();

            if (rs.next()) {
                // CÓ RỒI -> UPDATE CỘNG DỒN
                int currentQty = rs.getInt("quantity");
                int newQty = currentQty + quantity;
                String sqlUpdate = "UPDATE tblCart SET quantity=? WHERE userID=? AND courseID=?";
                ptm.close(); // Đóng statement cũ
                
                ptm = conn.prepareStatement(sqlUpdate);
                ptm.setInt(1, newQty);
                ptm.setString(2, userID);
                ptm.setString(3, courseID);
                ptm.executeUpdate();
            } else {
                // CHƯA CÓ -> INSERT
                String sqlInsert = "INSERT INTO tblCart(userID, courseID, quantity) VALUES(?,?,?)";
                ptm.close();
                
                ptm = conn.prepareStatement(sqlInsert);
                ptm.setString(1, userID);
                ptm.setString(2, courseID);
                ptm.setInt(3, quantity);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(ptm!=null) ptm.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
    }

    // Hàm remove và clear giữ nguyên như cũ...
    public void clearCart(String userID) {
        // ... code cũ của ông ...
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
    
    public boolean removeFromCart(String userID, String courseID) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM tblCart WHERE userID=? AND courseID=?";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                ptm.setString(2, courseID);
                
                // executeUpdate > 0 nghĩa là xóa thành công ít nhất 1 dòng
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }
    
    public void updateQuantity(String userID, String courseID, int quantity) throws SQLException, ClassNotFoundException {
    Connection conn = null;
    PreparedStatement ptm = null;
    try {
        conn = DBUtils.getConnection();
        if (conn != null) {
            // Nếu quantity <= 0 thì xóa khỏi DB
            if (quantity <= 0) {
                String sql = "DELETE FROM tblCart WHERE userID=? AND courseID=?";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                ptm.setString(2, courseID);
            } else {
                // Nếu > 0 thì update
                String sql = "UPDATE tblCart SET quantity=? WHERE userID=? AND courseID=?";
                ptm = conn.prepareStatement(sql);
                ptm.setInt(1, quantity);
                ptm.setString(2, userID);
                ptm.setString(3, courseID);
            }
            ptm.executeUpdate();
        }
    } finally {
        if (ptm != null) ptm.close();
        if (conn != null) conn.close();
    }
}
}
