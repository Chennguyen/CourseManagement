/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class UserDAO {

    private static final String LOGIN = "SELECT fullName, roleID FROM tblUsers WHERE userID=? AND password=?";
    private static final String CHECK_DUPLICATE = "SELECT userID FROM tblUsers WHERE userID=?";
    private static final String INSERT = "INSERT INTO tblUsers(userID, fullName, password, roleID) VALUES(?,?,?,?)";
    // Giữ nguyên các query khác

    public UserDTO checkLogin(String userID, String password) throws SQLException, ClassNotFoundException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(LOGIN);
                ptm.setString(1, userID);
                ptm.setString(2, password);
                rs = ptm.executeQuery();

                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String roleID = rs.getString("roleID"); // Lấy role từ DB

                    // Nạp roleID vào Constructor mới sửa
                    user = new UserDTO(userID, fullName, password, roleID);
                }
            }
        } finally {
            // Sửa lại đoạn này để an toàn hơn (hoặc dùng try-with-resources nếu được phép)
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
        return user;
    }
    
    public boolean checkDuplicate(String userID) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CHECK_DUPLICATE);
                ptm.setString(1, userID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    check = true; // Tìm thấy nghĩa là bị trùng
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    // 3. Hàm Insert User Mới (Dùng cho RegisterController) -> Bổ sung cái này nữa
    public boolean insert(UserDTO user) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(INSERT);
                ptm.setString(1, user.getUserID());
                ptm.setString(2, user.getFullName());
                ptm.setString(3, user.getPassword());
                ptm.setString(4, user.getRoleID());
                
                // executeUpdate trả về số dòng bị ảnh hưởng (>0 là thành công)
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }
}
