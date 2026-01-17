/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Lenovo
 */
public class DiscountDTO {
    private String code;
    private int percentDiscount;
    private Date expiryDate;
    private int quantity;

    public DiscountDTO() {
    }

    public DiscountDTO(String code, int percentDiscount, Date expiryDate, int quantity) {
        this.code = code;
        this.percentDiscount = percentDiscount;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getPercentDiscount() { return percentDiscount; }
    public void setPercentDiscount(int percentDiscount) { this.percentDiscount = percentDiscount; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
