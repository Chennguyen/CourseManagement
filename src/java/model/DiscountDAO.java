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
    // Tìm mã giảm giá trong DB
    public DiscountDTO getDiscount(String code) throws Exception {
        DiscountDTO dto = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "SELECT percentDiscount, expiryDate, quantity FROM tblDiscounts WHERE code=?";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, code);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    dto = new DiscountDTO(code, 
                                          rs.getInt("percentDiscount"), 
                                          rs.getDate("expiryDate"), 
                                          rs.getInt("quantity"));
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return dto;
    }
}
