<%@page import="model.CartItem"%>
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

            .total-row { font-weight: bold; font-size: 1.4em; color: red; }
            .subtotal-row { font-style: italic; color: #555; }
            .discount-row { color: green; font-weight: bold; }

            .btn-remove { color: red; font-weight: bold; text-decoration: none; }
            .btn-checkout { background: orange; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 1.1em;}
            .btn-back { background: #ddd; color: black; padding: 10px 20px; text-decoration: none; border-radius: 5px; }

            /* Style cho nút + - */
            .btn-qty { text-decoration: none; padding: 5px 10px; background: #e0e0e0; color: black; border-radius: 3px; font-weight: bold; }
            .btn-qty:hover { background: #ccc; }

            .discount-box {
                margin: 20px 0; padding: 15px; background-color: #f9f9f9;
                border: 1px dashed #4CAF50; width: 40%; border-radius: 5px;
            }
        </style>
    </head>
    <body>
        <h1>Your Shopping Cart</h1>

        <%
            Cart cart = (Cart) session.getAttribute("CART");

            // Check null và check size
            if (cart != null && cart.getCart() != null && !cart.getCart().isEmpty()) {
                // Tính toán sơ bộ
                float finalTotal = cart.getFinalTotal();
        %>
        <table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>Course Name</th>
                    <th>Lecturer</th>
                    <th>Price</th>
                    <th>Quantity</th> <%-- Cột Số lượng --%>
                    <th>Total (Volume Discounted)</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    int count = 1;
                    float tempSubTotal = 0;

                    for (CartItem item : cart.getCart().values()) {
                        CourseDTO dto = item.getCourse();
                        tempSubTotal += item.getTotal();
                %>
                <tr>
                    <td><%= count++%></td>
                    <td><%= dto.getName()%></td>
                    <td><%= dto.getLecturer()%></td>
                    <td>$<%= dto.getFee()%></td>

                    <%-- --- CẬP NHẬT: CỘT QUANTITY CÓ NÚT + - --- --%>
                    <td style="text-align: center; white-space: nowrap;">
                        <%-- Nút TRỪ --%>
                        <a href="MainController?action=UpdateCart&id=<%= dto.getId()%>&quantity=<%= item.getQuantity() - 1%>" 
                           class="btn-qty">-</a>

                        <span style="margin: 0 10px; font-weight: bold; font-size: 1.1em;"><%= item.getQuantity()%></span>

                        <%-- Nút CỘNG --%>
                        <a href="MainController?action=UpdateCart&id=<%= dto.getId()%>&quantity=<%= item.getQuantity() + 1%>" 
                           class="btn-qty">+</a>
                    </td>
                    <%-- ------------------------------------------ --%>

                    <%-- Cột Total của từng dòng --%>
                    <td>$<%= item.getTotal()%></td>

                    <td>
                        <a class="btn-remove" href="MainController?action=Remove&id=<%= dto.getId()%>" 
                           onclick="return confirm('Remove <%= dto.getName()%> from cart?');">Remove</a>
                    </td>
                </tr>
                <%
                    }
                %>

                <%-- CÁC DÒNG TỔNG TIỀN --%>
                <tr>
                    <td colspan="5" style="text-align: right;" class="subtotal-row">Subtotal (After Volume Discount):</td>
                    <td colspan="2">$<%= tempSubTotal%></td>
                </tr>

                <% if (cart.getDiscountPercent() > 0) {
                        float discountAmount = tempSubTotal * cart.getDiscountPercent() / 100;
                %>
                <tr>
                    <td colspan="5" style="text-align: right;" class="discount-row">
                        Voucher Discount (<%= cart.getDiscountPercent()%>%):
                    </td>
                    <td colspan="2" class="discount-row">-$<%= discountAmount%></td>
                </tr>
                <% }%>

                <tr>
                    <td colspan="5" style="text-align: right; font-weight: bold; font-size: 1.2em;">GRAND TOTAL:</td>
                    <td colspan="2" class="total-row">$<%= cart.getFinalTotal()%></td>
                </tr>
            </tbody>
        </table>

        <%-- FORM NHẬP VOUCHER --%>
        <div class="discount-box">
            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="Discount"/>
                <strong>Have a coupon? </strong>
                <input type="text" name="discountCode" placeholder="Enter code (e.g. JAVA2025)" required style="padding: 5px;"/>
                <input type="submit" value="Apply" style="padding: 5px 10px; cursor: pointer;"/>
            </form>

            <p style="color: red; margin: 5px 0; font-weight: bold;">${requestScope.DISCOUNT_ERROR}</p>
            <p style="color: green; margin: 5px 0; font-weight: bold;">${requestScope.DISCOUNT_MSG}</p>
        </div>

        <br/>
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