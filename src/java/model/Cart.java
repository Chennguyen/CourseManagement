/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public class Cart {
   private Map<String, CourseDTO> cart;
    private int discountPercent = 0; // Mặc định chưa giảm

    public Cart() {
    }

    public Cart(Map<String, CourseDTO> cart) {
        this.cart = cart;
    }

    public Map<String, CourseDTO> getCart() {
        return cart;
    }

    public void setCart(Map<String, CourseDTO> cart) {
        this.cart = cart;
    }

    // --- Getter Setter cho Discount ---
    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean add(CourseDTO course) {
        boolean check = false;
        if (this.cart == null) {
            this.cart = new HashMap<>();
        }
        if (this.cart.containsKey(course.getId())) {
            // Khóa học online thường chỉ mua 1 lần
        } else {
            this.cart.put(course.getId(), course);
            check = true;
        }
        return check;
    }

    public boolean remove(String id) {
        boolean check = false;
        if (this.cart != null) {
            if (this.cart.containsKey(id)) {
                this.cart.remove(id);
                // Nếu xóa sạch giỏ hàng thì reset mã giảm giá
                if (this.cart.isEmpty()) {
                    this.discountPercent = 0;
                }
                check = true;
            }
        }
        return check;
    }
    
    // --- LOGIC TÍNH TIỀN ---

    // 1. Tổng tiền gốc
    public float getTotal() {
        float total = 0;
        if (this.cart != null) {
            for (CourseDTO course : this.cart.values()) {
                total += course.getFee();
            }
        }
        return total;
    }
    
    // 2. Tiền được giảm
    public float getDiscountAmount() {
        return getTotal() * discountPercent / 100;
    }
    
    // 3. Tiền phải trả (Final) - Sẽ dùng để lưu xuống DB
    public float getFinalTotal() {
        return getTotal() - getDiscountAmount();
    }
}
