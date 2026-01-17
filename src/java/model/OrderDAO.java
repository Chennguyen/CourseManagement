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
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class OrderDAO {
    public boolean insertOrder(UserDTO user, Cart cart) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        boolean check = false;
        
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                // 1. TẮT AUTO COMMIT (Bắt đầu Transaction)
                conn.setAutoCommit(false); 

                // 2. Insert Bảng Order (Cha)
                String sqlOrder = "INSERT INTO tblOrders(userID, total) VALUES(?, ?)";
                
                // Cần lấy lại Key (OrderID) vừa sinh ra
                ptm = conn.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS);
                ptm.setString(1, user.getUserID());
                ptm.setFloat(2, cart.getFinalTotal());
                ptm.executeUpdate();

                // 3. Lấy OrderID vừa tạo
                rs = ptm.getGeneratedKeys();
                int orderID = 0;
                if (rs.next()) {
                    orderID = rs.getInt(1); // Lấy key tự tăng
                }

                // 4. Insert Bảng OrderDetails (Con)
                String sqlDetail = "INSERT INTO tblOrderDetails(orderID, courseID, price, quantity) VALUES(?,?,?,?)";
                ptm = conn.prepareStatement(sqlDetail);

                // Duyệt qua từng món trong giỏ để lưu
                for (CourseDTO dto : cart.getCart().values()) {
                    ptm.setInt(1, orderID);
                    ptm.setString(2, dto.getId());
                    ptm.setFloat(3, dto.getFee());
                    ptm.setInt(4, 1); // Khóa học online số lượng luôn là 1
                    ptm.addBatch(); // Gom lại chạy 1 lần cho nhanh
                }
                ptm.executeBatch();

                // 5. CHỐT ĐƠN (Commit)
                conn.commit();
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi thì hoàn tác lại mọi thứ, không lưu gì cả
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Mở lại Auto Commit cho các hàm khác dùng
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
            if (ptm != null) ptm.close();
            if (rs != null) rs.close();
        }
        return check;
    }
    
    public boolean hasUserBoughtCourse(String userID, String courseID) throws Exception {
        boolean result = false;
        Connection conn = DBUtils.getConnection();
        if (conn != null) {
            // SỬA CÂU QUERY: Thêm LIKE hoặc Trim để tránh lỗi so sánh chuỗi
            String sql = "SELECT O.orderID FROM tblOrders O " +
                         "JOIN tblOrderDetails D ON O.orderID = D.orderID " +
                         "WHERE O.userID = ? AND D.courseID LIKE ?"; // Dùng LIKE cho an toàn
            
            PreparedStatement ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            ptm.setString(2, courseID); // Truyền vào C-001
            
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                result = true;
            }
            conn.close();
        }
        return result;
    }
}
