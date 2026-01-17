<%-- 
    Document   : OrderSuccess
    Created on : Jan 17, 2026, 8:11:35 PM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order Success</title>
        <style>
            body { text-align: center; padding: 50px; font-family: Arial; }
            h1 { color: green; font-size: 50px; }
            .btn { background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; }
        </style>
    </head>
    <body>
        <h1>✅ Order Successful!</h1>
        <h3>Thank you, ${sessionScope.LOGIN_USER.fullName}</h3>
        <p>Your order has been saved to the database.</p>
        <br/>
        <a href="MainController?action=Search" class="btn">Continue Shopping</a>
    </body>
</html>
