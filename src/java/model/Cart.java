package model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    // SỬA LẠI: Dùng CartItem thay vì CourseDTO
    private Map<String, CartItem> cart;
    private int discountPercent = 0; 

    public Cart() {
    }

    public Cart(Map<String, CartItem> cart) {
        this.cart = cart;
    }

    public Map<String, CartItem> getCart() {
        return cart;
    }

    public void setCart(Map<String, CartItem> cart) {
        this.cart = cart;
    }
    
    // --- SỬA LẠI HÀM ADD: Nhận thêm int quantity ---
    public boolean add(CourseDTO course, int quantity) {
        if (this.cart == null) {
            this.cart = new HashMap<>();
        }
        
        if (this.cart.containsKey(course.getId())) {
            // Nếu đã có -> Cộng dồn số lượng
            CartItem item = this.cart.get(course.getId());
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Nếu chưa có -> Tạo CartItem mới
            this.cart.put(course.getId(), new CartItem(course, quantity));
        }
        return true;
    }

    public boolean remove(String id) {
        if (this.cart != null && this.cart.containsKey(id)) {
            this.cart.remove(id);
            if (this.cart.isEmpty()) this.discountPercent = 0;
            return true;
        }
        return false;
    }

    // --- SỬA LẠI TÍNH TỔNG TIỀN ---
    public float getFinalTotal() {
        float subTotal = 0;
        if (this.cart != null) {
            for (CartItem item : this.cart.values()) {
                subTotal += item.getTotal(); // Gọi hàm tính tiền của CartItem
            }
        }
        return subTotal * (1 - (discountPercent / 100.0f));
    }
    
    public void updateQuantity(String id, int newQuantity) {
    if (this.cart == null) return;
    
    if (this.cart.containsKey(id)) {
        if (newQuantity <= 0) {
            // Nếu số lượng <= 0 thì xóa luôn khỏi giỏ
            this.cart.remove(id);
             if (this.cart.isEmpty()) this.discountPercent = 0;
        } else {
            // Ngược lại thì cập nhật số lượng mới
            CartItem item = this.cart.get(id);
            item.setQuantity(newQuantity);
        }
    }
}
    
    // Getter Setter Discount
    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
}