/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Lenovo
 */
public class CourseDAO {

    private static final String GET_ALL = "SELECT id, name, description, duration, fee, lecturer, roadmap FROM tblCourses";
    private static final String SEARCH = "SELECT id, name, description, duration, fee, lecturer, roadmap FROM tblCourses WHERE name LIKE ?";
    private static final String DELETE = "DELETE FROM tblCourses WHERE id=?";
    private static final String UPDATE = "UPDATE tblCourses SET name=?, description=?, duration=?, fee=?, lecturer=?, roadmap=? WHERE id=?";
    private static final String GET_BY_ID = "SELECT id, name, description, duration, fee, lecturer, roadmap FROM tblCourses WHERE id=?";
    private static final String GET_FEEDBACKS = "SELECT feedbackID, userID, rating, comment, date FROM tblFeedbacks WHERE courseID=?";
    
    // Hàm lấy danh sách tất cả khóa học (Dùng cho LoginController khi login xong)
    public List<CourseDTO> getListCourses() throws SQLException, ClassNotFoundException {
        List<CourseDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ALL);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    int duration = rs.getInt("duration");
                    float fee = rs.getFloat("fee");

                    String lecturer = rs.getString("lecturer"); // Mới
                    String roadmap = rs.getString("roadmap");   // Mới

                    // Tạo DTO với Constructor đầy đủ 7 tham số
                    list.add(new CourseDTO(id, name, description, duration, fee, lecturer, roadmap));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }
    
    public boolean insertCourse(CourseDTO course) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ptm = null;
        boolean check = false;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                // Insert đủ 7 cột
                String sql = "INSERT INTO tblCourses(id, name, description, duration, fee, lecturer, roadmap) VALUES(?,?,?,?,?,?,?)";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, course.getId());
                ptm.setString(2, course.getName());
                ptm.setString(3, course.getDescription());
                ptm.setInt(4, course.getDuration());
                ptm.setFloat(5, course.getFee());
                ptm.setString(6, course.getLecturer());
                ptm.setString(7, course.getRoadmap());
                
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    public List<CourseDTO> searchCourse(String search) throws SQLException, ClassNotFoundException {
        List<CourseDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(SEARCH);
                // Tìm kiếm gần đúng: %keyword%
                ptm.setString(1, "%" + search + "%");
                rs = ptm.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    int duration = rs.getInt("duration");
                    float fee = rs.getFloat("fee");

                    String lecturer = rs.getString("lecturer");
                    String roadmap = rs.getString("roadmap");

                    list.add(new CourseDTO(id, name, description, duration, fee, lecturer, roadmap));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    public boolean deleteCourse(String id) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ptm = null;
        boolean check = false;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(DELETE);
                ptm.setString(1, id);
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

// 2. Hàm Update
    public boolean updateCourse(CourseDTO course) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ptm = null;
        boolean check = false;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE);
                ptm.setString(1, course.getName());
                ptm.setString(2, course.getDescription());
                ptm.setInt(3, course.getDuration());
                ptm.setFloat(4, course.getFee());
                ptm.setString(5, course.getLecturer()); 
                ptm.setString(6, course.getRoadmap());
                ptm.setString(7, course.getId());
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

// 3. Hàm lấy thông tin 1 khóa học (để hiển thị lên form sửa)
    public CourseDTO getCourseByID(String id) throws SQLException, ClassNotFoundException {
        CourseDTO course = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_BY_ID);
                ptm.setString(1, id);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    int duration = rs.getInt("duration");
                    float fee = rs.getFloat("fee");
                    String lecturer = rs.getString("lecturer");
                    String roadmap = rs.getString("roadmap");
                    
                    course = new CourseDTO(id, name, description, duration, fee, lecturer, roadmap);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return course;
    }
    
    // MỚI: Hàm lấy Feedback cho trang Show More Info
    public List<FeedbackDTO> getFeedbacks(String courseID) throws SQLException, ClassNotFoundException {
        List<FeedbackDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_FEEDBACKS);
                ptm.setString(1, courseID);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int feedbackID = rs.getInt("feedbackID");
                    String userID = rs.getString("userID");
                    int rating = rs.getInt("rating");
                    String comment = rs.getString("comment");
                    java.sql.Date date = rs.getDate("date");
                    
                    list.add(new FeedbackDTO(feedbackID, courseID, userID, rating, comment, date));
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return list;
    }
    
    public boolean insertFeedback(FeedbackDTO fb) throws Exception {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO tblFeedbacks(courseID, userID, rating, comment, date) VALUES(?,?,?,?, GETDATE())";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, fb.getCourseID());
                ptm.setString(2, fb.getUserID());
                ptm.setInt(3, fb.getRating());
                ptm.setString(4, fb.getComment());
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }
}
