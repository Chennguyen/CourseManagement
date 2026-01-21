<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order Success</title>
        <style>
            body { text-align: center; padding: 50px; font-family: Arial; }
            h1 { color: green; font-size: 50px; }
            .btn { background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; }
            
            /* CSS cho danh sách Key */
            .license-box {
                background: #f4f4f4;
                border: 2px dashed #4CAF50;
                width: 50%;
                margin: 20px auto;
                padding: 20px;
                text-align: left;
                border-radius: 10px;
            }
            .license-item {
                font-size: 1.2em;
                margin: 10px 0;
                font-family: "Courier New", Courier, monospace; /* Font giống code */
                font-weight: bold;
                color: #333;
                border-bottom: 1px solid #ddd;
                padding-bottom: 5px;
            }
            .note { color: red; font-style: italic; font-size: 0.9em; }
        </style>
    </head>
    <body>
        <h1>✅ Order Successful!</h1>
        <h3>Thank you, ${sessionScope.LOGIN_USER.fullName}</h3>
        <p>Your order has been saved to the database.</p>

        <%-- HIỂN THỊ LICENSE KEY --%>
        <%
            List<String> licenses = (List<String>) request.getAttribute("LICENSES");
            if (licenses != null && !licenses.isEmpty()) {
        %>
            <div class="license-box">
                <h3 style="text-align: center; color: #4CAF50;">Your Activation Keys</h3>
                <p class="note">*Please save these keys immediately. You can share them with your friends.</p>
                
                <% for (String key : licenses) { %>
                    <div class="license-item">🔑 <%= key %></div>
                <% } %>
            </div>
        <%
            }
        %>
        
        <br/>
        <a href="MainController?action=Search" class="btn">Continue Shopping</a>
    </body>
</html>