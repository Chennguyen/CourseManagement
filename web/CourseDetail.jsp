<%-- 
    Document   : CourseDetail
    Created on : Jan 17, 2026, 6:12:36 PM
    Author     : Lenovo
--%>

<%@page import="model.FeedbackDTO"%>
<%@page import="model.CourseDTO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Detail</title>
        <style>
            /* Thêm chút CSS cho Form đẹp hơn */
            .review-form {
                background-color: #f9f9f9;
                padding: 20px;
                border: 1px dashed #2E8B57;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .review-form h4 { margin-top: 0; color: #2E8B57; }
            .review-form textarea { width: 100%; padding: 8px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px; }
            .btn-submit { background-color: #2E8B57; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
            .btn-submit:hover { background-color: #246b43; }
            
            /* CSS cũ */
            .feedback-item { border-bottom: 1px solid #eee; padding: 10px 0; }
            .star { color: orange; }
            .btn-buy { background: orange; color: white; padding: 10px 20px; text-decoration: none; font-weight: bold; border-radius: 5px; }
            .btn-back { background: #ddd; color: black; padding: 10px 20px; text-decoration: none; border-radius: 5px; }
            .container { max-width: 800px; margin: 20px auto; font-family: Arial, sans-serif; }
            .roadmap { background: #f0f0f0; padding: 15px; border-left: 5px solid #2E8B57; white-space: pre-line; }
        </style>
    </head>
    <body>
        <div class="container">
            <%
                CourseDTO course = (CourseDTO) request.getAttribute("COURSE_DETAIL");
                if (course != null) {
            %>
                <h1><%= course.getName() %> (ID: <%= course.getId() %>)</h1>
                <p><b>Lecturer:</b> <%= course.getLecturer() %></p>
                <p><b>Fee:</b> <span style="color: red; font-size: 1.2em">$<%= course.getFee() %></span></p>
                <p><b>Description:</b> <%= course.getDescription() %></p>
                
                <h3>🚀 Road map </h3>
                <div class="roadmap"><%= course.getRoadmap() %></div>
                <hr/>

                <h3>⭐ Student Feedbacks</h3>

                <%-- 2. FORM VIẾT REVIEW (MỚI THÊM) --%>
                <%
                    // Lấy biến cờ hiệu từ Controller
                    Boolean canReview = (Boolean) request.getAttribute("CAN_REVIEW");
                    
                    // Nếu user được phép review (đã mua hàng)
                    if (canReview != null && canReview) {
                %>
                    <div class="review-form">
                        <h4>✍️ Write your review</h4>
                        <form action="MainController" method="POST">
                            <input type="hidden" name="action" value="SubmitFeedback"/>
                            <input type="hidden" name="id" value="<%= course.getId() %>"/>
                            
                            <label><b>Rating:</b></label>
                            <select name="rating">
                                <option value="5">⭐⭐⭐⭐⭐ (Excellent)</option>
                                <option value="4">⭐⭐⭐⭐ (Good)</option>
                                <option value="3">⭐⭐⭐ (Average)</option>
                                <option value="2">⭐⭐ (Poor)</option>
                                <option value="1">⭐ (Terrible)</option>
                            </select>
                            <br/>
                            
                            <textarea name="comment" rows="3" placeholder="Share your experience with this course..." required></textarea>
                            <br/>
                            
                            <input type="submit" value="Post Review" class="btn-submit"/>
                        </form>
                    </div>
                <% 
                    } else { 
                %>
                    <p style="color: gray; font-style: italic; background: #eee; padding: 10px;">
                        🔒 You must buy this course to write a review.
                    </p>
                <% 
                    } 
                %>
                <%-- KẾT THÚC FORM --%>

                <%-- 3. DANH SÁCH FEEDBACK CŨ --%>
                <%
                    List<FeedbackDTO> listFB = (List<FeedbackDTO>) request.getAttribute("FEEDBACK_LIST");
                    if (listFB != null && !listFB.isEmpty()) {
                        for (FeedbackDTO fb : listFB) {
                %>
                    <div class="feedback-item">
                        <strong>User: <%= fb.getUserID() %></strong> 
                        <span class="star"><% for(int i=0; i<fb.getRating(); i++) { %>★<% } %></span><br/>
                        <span>"<%= fb.getComment() %>"</span> <br/>
                        <small style="color: #888"><%= fb.getDate() %></small>
                    </div>
                <%
                        }
                    } else {
                %>
                    <p style="color: gray;">No reviews yet.</p>
                <% } %>
                
                <br/><br/>
                
                <%-- 4. CÁC NÚT ĐIỀU HƯỚNG --%>
                <%-- Logic ẩn hiện nút Add to Cart: Nếu đã mua rồi (canReview=true) thì ẩn nút mua đi cho đỡ nhầm --%>
                <% if (canReview == null || !canReview) { %>
                    <a href="MainController?action=AddToCart&id=<%= course.getId() %>" class="btn-buy">Add To Cart</a>
                <% } else { %>
                    <span style="color: green; font-weight: bold; margin-right: 15px;">✅ You own this course</span>
                <% } %>

                <a href="MainController?action=Search" class="btn-back">Back to List</a>

            <% } else { %>
                <h2 style="color: red">Course Not Found!</h2>
                <a href="MainController?action=Search">Back</a>
            <% } %>
        </div>
    </body>
</html>
