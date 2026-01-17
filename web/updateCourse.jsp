<%-- 
    Document   : updateCourse
    Created on : Jan 17, 2026, 11:29:16 AM
    Author     : Lenovo
--%>

<%@page import="model.CourseDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Course</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; padding: 40px; margin: 0; }
            .form-card { background-color: white; width: 600px; padding: 40px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
            
            h1 { text-align: center; color: #333; margin-top: 0; font-size: 24px; border-bottom: 2px solid #2E8B57; padding-bottom: 15px; display: inline-block; width: 100%; }
            
            .form-group { margin-bottom: 15px; }
            label { display: block; font-weight: 600; margin-bottom: 5px; color: #555; }
            input[type="text"], input[type="number"], textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; font-family: inherit; font-size: 14px; }
            input[type="text"]:focus, input[type="number"]:focus, textarea:focus { border-color: #2E8B57; outline: none; }
            input[readonly] { background-color: #e9ecef; cursor: not-allowed; color: #666; }
            
            .btn-save { background-color: #2E8B57; color: white; border: none; padding: 12px 20px; width: 100%; border-radius: 4px; cursor: pointer; font-size: 16px; font-weight: bold; margin-top: 20px; transition: 0.3s; }
            .btn-save:hover { background-color: #246b43; }
            
            .btn-cancel { display: block; text-align: center; margin-top: 15px; color: #666; text-decoration: none; font-size: 14px; }
            .btn-cancel:hover { text-decoration: underline; color: #333; }
            
            .error-msg { color: #dc3545; text-align: center; margin-top: 10px; }
        </style>
    </head>
    <body>
        <%
            CourseDTO course = (CourseDTO) request.getAttribute("COURSE_DETAIL");
            if (course != null) {
        %>
        <div class="form-card">
            <h1>Update Course Information</h1>
            
            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="Update"/>
                <input type="hidden" name="actionType" value="Save"/>

                <div class="form-group">
                    <label>Course ID</label>
                    <input type="text" name="id" value="<%= course.getId()%>" readonly/>
                </div>

                <div class="form-group">
                    <label>Course Name</label>
                    <input type="text" name="name" value="<%= course.getName()%>" required/>
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <input type="text" name="description" value="<%= course.getDescription()%>" required/>
                </div>

                <div class="form-group" style="display: flex; gap: 20px;">
                    <div style="flex: 1;">
                        <label>Duration (weeks)</label>
                        <input type="number" name="duration" value="<%= course.getDuration()%>" required/>
                    </div>
                    <div style="flex: 1;">
                        <label>Fee ($)</label>
                        <input type="number" step="0.1" name="fee" value="<%= course.getFee()%>" required/>
                    </div>
                </div>

                <div class="form-group">
                    <label>Lecturer</label>
                    <input type="text" name="lecturer" value="<%= course.getLecturer()%>" required/>
                </div>

                <div class="form-group">
                    <label>Roadmap</label>
                    <textarea name="roadmap" rows="5" required><%= course.getRoadmap()%></textarea>
                </div>

                <input type="submit" value="Save Changes" class="btn-save"/>
                <a href="MainController?action=Search" class="btn-cancel">Cancel and Go Back</a>
            </form>
            <h3 class="error-msg">${requestScope.ERROR}</h3>
        </div>
        <% } %>
    </body>
</html>
