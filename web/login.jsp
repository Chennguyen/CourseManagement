<%-- 
    Document   : login
    Created on : Jan 17, 2026, 10:20:10 AM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <style>
            /* 1. Cấu trúc trang */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* Font đẹp hơn */
                background-color: #f0f2f5; /* Màu xám hiện đại của Facebook */
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }

            /* 2. Khung đăng nhập */
            .login-container {
                background-color: white;
                padding: 40px; /* Tăng padding cho thoáng */
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* Bóng đổ xịn hơn */
                width: 320px;
            }

            /* 3. Text Welcome mới thêm */
            .welcome-title {
                text-align: center;
                color: #2E8B57; /* Màu xanh lá đậm sang trọng */
                margin: 0 0 5px 0;
                font-size: 28px;
            }
            
            .sub-title {
                text-align: center;
                color: #666;
                font-size: 14px;
                margin-bottom: 30px; /* Cách xa form ra một chút */
            }

            /* 4. Form inputs */
            label {
                font-weight: bold;
                color: #333;
                display: block;
                margin-bottom: 5px;
            }

            input[type="text"], input[type="password"] {
                width: 100%;
                padding: 12px;
                margin-bottom: 15px;
                border: 1px solid #ddd;
                border-radius: 5px;
                box-sizing: border-box;
                transition: border 0.3s;
            }

            input[type="text"]:focus, input[type="password"]:focus {
                border-color: #2E8B57; /* Focus vào đổi màu xanh */
                outline: none;
            }

            /* 5. Buttons */
            input[type="submit"] {
                width: 100%;
                background-color: #2E8B57;
                color: white;
                padding: 12px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                font-size: 16px;
                margin-top: 10px;
                transition: background 0.3s;
            }

            input[type="submit"]:hover {
                background-color: #246b43;
            }

            .btn-reset {
                background: none;
                border: none;
                color: #888;
                text-decoration: none;
                cursor: pointer;
                margin-top: 15px;
                display: block;
                width: 100%;
                text-align: center;
                font-size: 14px;
            }
            .btn-reset:hover {
                color: #555;
                text-decoration: underline;
            }

            .error-msg {
                color: #dc3545;
                text-align: center;
                margin-top: 20px;
                background: #ffe6e6;
                padding: 10px;
                border-radius: 5px;
                font-size: 14px;
                border: 1px solid #ffcccc;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h1 class="welcome-title">Welcome Back! </h1>
            <p class="sub-title">Sign in to Course Management</p>

            <form action="MainController" method="POST">
                <label>User ID</label>
                <input type="text" name="userID" placeholder="Enter your ID" required=""/>
                
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required=""/>
                
                <input type="submit" name="action" value="Login"/>
                
                <button type="reset" class="btn-reset">Clear information</button>
            </form>
            
            <%
                String error = (String) request.getAttribute("ERROR");
                if (error != null) {
            %>
                <div class="error-msg">⚠️ <%= error %></div>
            <%
                }
            %>
        </div>
    </body>
</html>