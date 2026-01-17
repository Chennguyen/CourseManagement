<%-- 
    Document   : ViewCart
    Created on : Jan 17, 2026, 7:54:34 PM
    Author     : Lenovo
--%>

<%@page import="model.Cart"%>
<%@page import="model.CourseDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Cart</title>
        <style>
            body { font-family: Arial, sans-serif; padding: 20px; }
            table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
            th { background-color: #4CAF50; color: white; }
            
            /* CSS cho các dòng tổng tiền */
            .total-row { font-weight: bold; font-size: 1.4em; color: red; }
            .subtotal-row { font-style: italic; color: #555; }
            .discount-row { color: green; font-weight: bold; }

            /* CSS cho nút bấm */
            .btn-remove { color: red; font-weight: bold; text-decoration: none; }
            .btn-checkout { background: orange; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 1.1em;}
            .btn-back { background: #ddd; color: black; padding: 10px 20px; text-decoration: none; border-radius: 5px; }
            
            /* CSS cho khung nhập mã giảm giá */
            .discount-box {
                margin: 20px 0;
                padding: 15px;
                background-color: #f9f9f9;
                border: 1px dashed #4CAF50;
                width: 40%;
                border-radius: 5px;
            }
            .discount-box input[type="text"] { padding: 8px; width: 60%; border: 1px solid #ccc; border-radius: 4px; }
            .discount-box input[type="submit"] { padding: 8px 15px; background: #333; color: white; border: none; border-radius: 4px; cursor: pointer; }
            .discount-box input[type="submit"]:hover { background: #555; }
        </style>
    </head>
    <body>
        <h1>Your Shopping Cart</h1>
        
        <%
            // Lấy giỏ hàng từ Session
            Cart cart = (Cart) session.getAttribute("CART");
            
            // Kiểm tra giỏ hàng có tồn tại và có đồ không
            if (cart != null && cart.getCart() != null && cart.getCart().size() > 0) {
        %>
            <table>
                <thead>
                    <tr>
                        <th>No</th>
                        <th>ID</th>
                        <th>Course Name</th>
                        <th>Lecturer</th>
                        <th>Fee ($)</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int count = 1;
                        // Duyệt qua các item trong giỏ
                        for (CourseDTO dto : cart.getCart().values()) {
                    %>
                    <tr>
                        <td><%= count++ %></td>
                        <td><%= dto.getId() %></td>
                        <td><%= dto.getName() %></td>
                        <td><%= dto.getLecturer() %></td>
                        <td><%= dto.getFee() %></td>
                        <td>
                            <a class="btn-remove" href="MainController?action=Remove&id=<%= dto.getId() %>" 
                               onclick="return confirm('Remove <%= dto.getName() %> from cart?');">Remove</a>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    
                    <%-- 1. HIỂN THỊ CÁC DÒNG TÍNH TIỀN (LOGIC MỚI) --%>
                    
                    <tr>
                        <td colspan="4" style="text-align: right;" class="subtotal-row">Subtotal:</td>
                        <td colspan="2">$<%= cart.getTotal() %></td>
                    </tr>

                    <% if (cart.getDiscountPercent() > 0) { %>
                    <tr>
                        <td colspan="4" style="text-align: right;" class="discount-row">
                            Discount (<%= cart.getDiscountPercent() %>%):
                        </td>
                        <td colspan="2" class="discount-row">-$<%= cart.getDiscountAmount() %></td>
                    </tr>
                    <% } %>

                    <tr>
                        <td colspan="4" style="text-align: right; font-weight: bold; font-size: 1.2em;">Total To Pay:</td>
                        <td colspan="2" class="total-row">$<%= cart.getFinalTotal() %></td>
                    </tr>
                </tbody>
            </table>
            
            <%-- 2. KHUNG NHẬP MÃ GIẢM GIÁ (MỚI THÊM) --%>
            <div class="discount-box">
                <form action="MainController" method="POST">
                    <input type="hidden" name="action" value="Discount"/>
                    <strong>Have a coupon? </strong>
                    <input type="text" name="discountCode" placeholder="Enter code (e.g. JAVA2025)" required/>
                    <input type="submit" value="Apply"/>
                </form>
                
                <%-- Hiển thị thông báo Lỗi hoặc Thành công --%>
                <p style="color: red; margin: 5px 0; font-weight: bold;">${requestScope.DISCOUNT_ERROR}</p>
                <p style="color: green; margin: 5px 0; font-weight: bold;">${requestScope.DISCOUNT_MSG}</p>
            </div>
            
            <br/>
            <%-- Nút Checkout và Back --%>
            <a href="MainController?action=Checkout" class="btn-checkout">Checkout Now</a>
            <a href="MainController?action=Search" class="btn-back">Continue Shopping</a>
            
        <%
            } else {
        %>
            <h3 style="color: gray;">Your cart is empty!</h3>
            <a href="MainController?action=Search" class="btn-back">Go to Course List</a>
        <%
            }
        %>
    </body>
</html>
