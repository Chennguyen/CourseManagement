package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // <--- 1. NHỚ IMPORT CÁI NÀY
import utils.DBUtils;

public class OrderDAO {

    public boolean insertOrder(UserDTO user, Cart cart) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ptm = null;
        PreparedStatement ptmDetail = null;
        PreparedStatement ptmLicense = null;
        ResultSet rs = null;
        boolean check = false;

        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                conn.setAutoCommit(false); // 1. Bắt buộc có Transaction

                // --- 2. INSERT ORDER ---
                // Tui thêm GETDATE() vào SQL để DB tự lấy ngày giờ hiện tại
                String sqlOrder = "INSERT INTO tblOrders(userID, total, date) VALUES(?, ?, GETDATE())";
                ptm = conn.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS);
                ptm.setString(1, user.getUserID());
                ptm.setFloat(2, cart.getFinalTotal());
                ptm.executeUpdate();

                rs = ptm.getGeneratedKeys();
                int orderID = 0;
                if (rs.next()) {
                    orderID = rs.getInt(1);
                }

                // --- 3. INSERT DETAIL & LICENSE ---
                String sqlDetail = "INSERT INTO tblOrderDetails(orderID, courseID, price, quantity) VALUES(?,?,?,?)";
                String sqlLicense = "INSERT INTO tblLicenses(code, courseID, orderID, status) VALUES(?,?,?,'Unused')";

                ptmDetail = conn.prepareStatement(sqlDetail);
                ptmLicense = conn.prepareStatement(sqlLicense);

                // Duyệt CartItem
                for (CartItem item : cart.getCart().values()) {
                    // Insert Detail
                    ptmDetail.setInt(1, orderID);
                    ptmDetail.setString(2, item.getCourse().getId());
                    // Lưu ý: Đây là giá gốc. Nếu muốn lưu giá đã giảm thì lấy item.getTotal() / quantity
                    ptmDetail.setFloat(3, item.getCourse().getFee());
                    ptmDetail.setInt(4, item.getQuantity());
                    ptmDetail.executeUpdate(); // Insert Detail từng dòng

                    // --- SINH LICENSE ---
                    for (int i = 0; i < item.getQuantity(); i++) {
                        String randomCode = "JV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                        ptmLicense.setString(1, randomCode);
                        ptmLicense.setString(2, item.getCourse().getId());
                        ptmLicense.setInt(3, orderID);
                        ptmLicense.addBatch(); // Gom lại
                    }
                }

                // Chạy lệnh insert license một lần cho tất cả
                ptmLicense.executeBatch();

                conn.commit(); // 4. Chốt đơn
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // --- 5. BẮT BUỘC PHẢI ĐÓNG KẾT NỐI ---
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (ptmDetail != null) {
                ptmDetail.close();
            }
            if (ptmLicense != null) {
                ptmLicense.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                conn.close();
            }
        }
        return check;
    }

    // Hàm này giữ nguyên ok rồi
    public boolean hasUserBoughtCourse(String userID, String courseID) throws Exception {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "SELECT O.orderID FROM tblOrders O "
                        + "JOIN tblOrderDetails D ON O.orderID = D.orderID "
                        + "WHERE O.userID = ? AND D.courseID = ?"; // Dùng = cho chính xác, bỏ LIKE đi cho chuẩn

                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                ptm.setString(2, courseID);

                rs = ptm.executeQuery();
                if (rs.next()) {
                    result = true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public List<String> getNewestLicenseKeys(String userID) throws SQLException, ClassNotFoundException {
        List<String> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                // Lấy OrderID mới nhất của user này
                String sql = "SELECT L.code, C.name FROM tblLicenses L "
                        + "JOIN tblOrderDetails D ON L.orderID = D.orderID AND L.courseID = D.courseID "
                        + "JOIN tblCourses C ON D.courseID = C.id "
                        + "WHERE L.orderID = (SELECT MAX(orderID) FROM tblOrders WHERE userID = ?)";

                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    String courseName = rs.getString("name");
                    String code = rs.getString("code");
                    list.add(courseName + ": " + code);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }
}
