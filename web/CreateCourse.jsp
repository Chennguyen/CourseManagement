<%-- 
    Document   : CreateCourse
    Created on : Jan 17, 2026, 9:55:16 PM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Course</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; padding: 50px; }
            .form-container { background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 500px; }
            h2 { text-align: center; color: #2E8B57; margin-bottom: 30px; }
            
            label { font-weight: bold; display: block; margin-top: 15px; color: #333; }
            input[type="text"], input[type="number"], textarea { width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; }
            
            input[type="submit"] { background-color: #2E8B57; color: white; padding: 12px; border: none; border-radius: 5px; cursor: pointer; width: 100%; margin-top: 25px; font-weight: bold; font-size: 16px; }
            input[type="submit"]:hover { background-color: #246b43; }
            
            .btn-back { display: block; text-align: center; margin-top: 15px; text-decoration: none; color: #666; }
            .error { color: red; text-align: center; margin-bottom: 15px; }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>✨ Add New Course</h2>
            
            <%-- Hiện lỗi nếu trùng ID --%>
            <div class="error">${requestScope.ERROR}</div>

            <form action="MainController" method="POST">
                <label>Course ID (e.g., C-006):</label>
                <input type="text" name="id" required placeholder="Unique ID"/>

                <label>Course Name:</label>
                <input type="text" name="name" required placeholder="e.g. Python for Data Science"/>

                <label>Lecturer:</label>
                <input type="text" name="lecturer" required placeholder="Name of teacher"/>

                <label>Fee ($):</label>
                <input type="number" name="fee" step="0.1" required placeholder="100.0"/>

                <label>Duration (Weeks):</label>
                <input type="number" name="duration" required placeholder="10"/>

                <label>Description:</label>
                <textarea name="description" rows="2" required></textarea>

                <label>Roadmap:</label>
                <textarea name="roadmap" rows="3" placeholder="Week 1: ... Week 2: ..."></textarea>

                <input type="submit" name="action" value="Create"/>
                <a href="MainController?action=Search" class="btn-back">Cancel & Go Back</a>
            </form>
        </div>
    </body>
</html>
