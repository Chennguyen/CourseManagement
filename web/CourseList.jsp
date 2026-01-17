<%-- 
    Document   : CourseList
    Created on : Jan 17, 2026, 10:58:05 AM
    Author     : Lenovo
--%>

<%@page import="model.UserDTO"%>
<%@page import="model.CourseDTO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course List</title>
        <style>
            /* Reset & Font */
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; padding: 30px; margin: 0; color: #333; }
            
            /* Header Section */
            .header { display: flex; justify-content: space-between; align-items: center; background: white; padding: 20px 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 25px; }
            .header h2 { margin: 0; color: #333; font-size: 22px; }
            .header .user-actions { display: flex; align-items: center; gap: 20px; font-size: 14px; }
            .btn-create { background-color: #2E8B57; color: white; padding: 8px 16px; text-decoration: none; border-radius: 4px; font-weight: 600; font-size: 14px; transition: 0.3s; }
            .btn-create:hover { background-color: #246b43; }
            .btn-logout { color: #dc3545; text-decoration: none; font-weight: 600; }
            .btn-cart { color: #333; text-decoration: none; font-weight: 600; }
            
            /* Search & Message */
            .search-bar { margin-bottom: 20px; display: flex; gap: 10px; }
            .search-bar input[type="text"] { padding: 10px; border: 1px solid #ddd; border-radius: 4px; width: 300px; font-size: 14px; }
            .search-bar input[type="submit"] { padding: 10px 20px; background-color: #555; color: white; border: none; border-radius: 4px; cursor: pointer; }
            .success-msg { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 12px; border-radius: 4px; margin-bottom: 20px; }

            /* Table Styling */
            table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
            th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; }
            th { background-color: #333; color: white; font-weight: 500; text-transform: uppercase; font-size: 13px; letter-spacing: 0.5px; }
            tr:hover { background-color: #f8f9fa; } /* Hiệu ứng hover dòng */
            tr:last-child td { border-bottom: none; }
            
            /* Action Links */
            a { text-decoration: none; transition: 0.2s; }
            .link-show { color: #2E8B57; font-weight: 600; }
            .link-action { color: #007bff; font-weight: 600; }
            .link-delete { color: #dc3545; font-weight: 600; margin-left: 10px; }
            a:hover { text-decoration: underline; }
        </style>
    </head>
    <body>
        <%-- 1. KIỂM TRA LOGIN --%>
        <%
            if (session.getAttribute("LOGIN_USER") == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            boolean isAdmin = "AD".equals(user.getRoleID());
            model.Cart cart = (model.Cart) session.getAttribute("CART");
            int cartSize = (cart != null && cart.getCart() != null) ? cart.getCart().size() : 0;
        %>

        <%-- 2. HEADER --%>
        <div class="header">
            <div>
                <h2>Welcome, <%= user.getFullName() %> 👋</h2>
            </div>
            <div class="user-actions">
                <% if (isAdmin) { %>
                    <a href="CreateCourse.jsp" class="btn-create">+ New Course</a>
                <% } %>
                
                <a href="MainController?action=ViewCart" class="btn-cart">🛒 Cart (<%= cartSize %>)</a>
                <a href="MainController?action=Logout" class="btn-logout">Logout</a>
            </div>
        </div>

        <%-- 3. THÔNG BÁO --%>
        <%
            String msg = (String) session.getAttribute("MESSAGE");
            if (msg != null) {
        %>
            <div class="success-msg">✅ <%= msg %></div>
            <% session.removeAttribute("MESSAGE"); %>
        <% } %>

        <%-- 4. FORM TÌM KIẾM --%>
        <form action="MainController" method="POST" class="search-bar">
            <input type="text" name="search" value="${param.search}" placeholder="Search by course name..."/>
            <input type="submit" name="action" value="Search"/>
        </form>

        <%-- 5. DANH SÁCH KHÓA HỌC --%>
        <%
            List<CourseDTO> list = (List<CourseDTO>) request.getAttribute("LIST_COURSE");
            if (list != null && !list.isEmpty()) {
        %>
        <table>
            <thead>
                <tr>
                    <th style="width: 10%;">ID</th>
                    <th style="width: 25%;">Name</th>
                    <th style="width: 15%;">Fee</th>
                    <th style="width: 15%;">Info</th>
                    <th style="width: 35%;">Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    String search = request.getParameter("search");
                    if (search == null) search = "";
                    for (CourseDTO c : list) {
                %>
                <tr>
                    <td><b><%= c.getId() %></b></td>
                    <td><%= c.getName() %></td>
                    <td>$<%= c.getFee() %></td>
                    
                    <td><a href="MainController?action=ViewDetail&id=<%= c.getId() %>" class="link-show">View Detail</a></td>
                    
                    <td>
                        <% if (isAdmin) { %>
                            <a href="MainController?action=Update&id=<%= c.getId() %>" class="link-action">✏️ Edit</a>
                            <a href="MainController?action=Delete&id=<%= c.getId() %>" 
                               onclick="return confirm('Delete course <%= c.getName() %>?');" class="link-delete">🗑️ Delete</a>
                        <% } else { %>
                            <a href="MainController?action=AddToCart&id=<%= c.getId() %>&lastSearch=<%= search %>" class="link-action">
                                + Add to Cart
                            </a>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
            <h3 style="color: #666; text-align: center; margin-top: 50px;">No courses found matching your criteria.</h3>
        <% } %>
    </body>
</html>