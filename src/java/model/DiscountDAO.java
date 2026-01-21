/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class DiscountDAO {
    public DiscountDTO getDiscount(String code) {
        DiscountDTO dto = null;
        try {
            Connection conn = DBUtils.getConnection();
            if (conn != null) {
                // Sửa tên cột cho khớp DTO: code, percentDiscount...
                String sql = "SELECT code, percentDiscount, expiryDate, quantity FROM tblDiscounts WHERE code=?";
                PreparedStatement ptm = conn.prepareStatement(sql);
                ptm.setString(1, code);
                ResultSet rs = ptm.executeQuery();
                if (rs.next()) {
                    dto = new DiscountDTO(
                        rs.getString("code"),
                        rs.getInt("percentDiscount"),
                        rs.getDate("expiryDate"),
                        rs.getInt("quantity")
                    );
                }
                conn.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dto;
    }
}
