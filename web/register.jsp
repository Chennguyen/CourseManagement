<%-- 
    Document   : register
    Created on : Jan 20, 2026, 1:51:01 PM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <style>
            /* CSS đơn giản cho dòng thông báo lỗi màu đỏ */
            .error-msg {
                color: red;
                font-style: italic;
                font-size: 0.9em;
            }
        </style>
    </head>
    <body>
        <h1>Create New Account</h1>
        
        <form action="MainController" method="POST">
            
            User ID: <input type="text" name="userID" value="${param.userID}" required=""/>
            <span class="error-msg">${requestScope.USER_ERROR.userID}</span>
            <br/><br/>

            Full Name: <input type="text" name="fullName" value="${param.fullName}" required=""/>
            <span class="error-msg">${requestScope.USER_ERROR.fullName}</span>
            <br/><br/>

            Password: <input type="password" name="password" required=""/>
            <span class="error-msg">${requestScope.USER_ERROR.password}</span>
            <br/><br/>

            Confirm: <input type="password" name="confirm" required=""/>
            <span class="error-msg">${requestScope.USER_ERROR.confirm}</span>
            <br/><br/>

            <input type="submit" name="action" value="Register"/>
            <input type="reset" value="Reset"/>
        </form>

        <br/>
        <a href="login.jsp">Already have an account? Login here</a>
    </body>
</html>
