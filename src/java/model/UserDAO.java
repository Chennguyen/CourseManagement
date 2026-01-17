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

    // Giữ nguyên các query khác
    private static final String SEARCH_BY_PRICE = "SELECT id, name, price, model FROM tblPhones WHERE price >= ? ORDER BY price ASC";
    private static final String DUPLICATE = "SELECT name FROM tblPhones WHERE id=?";

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
}
