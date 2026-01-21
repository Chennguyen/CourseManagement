/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

public class CartItem {
    private CourseDTO course;
    private int quantity;

    public CartItem(CourseDTO course, int quantity) {
        this.course = course;
        this.quantity = quantity;
    }

    // --- LOGIC TÍNH TIỀN VOLUME DISCOUNT ---
    public float getTotal() {
        float originalTotal = course.getFee() * quantity;
        float discountRate = 0;
        
        // Logic: Mua >= 5 giảm 10%, >= 2 giảm 5%
        if (quantity >= 5) {
            discountRate = 0.10f; 
        } else if (quantity >= 2) {
            discountRate = 0.05f;
        }
        return originalTotal * (1 - discountRate);
    }

    // Getter Setter
    public CourseDTO getCourse() { return course; }
    public void setCourse(CourseDTO course) { this.course = course; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}