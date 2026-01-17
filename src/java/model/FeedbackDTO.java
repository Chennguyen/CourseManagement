/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Lenovo
 */
public class FeedbackDTO {
    private int feedbackID;
    private String courseID; // <--- THÊM CÁI NÀY
    private String userID; 
    private int rating;
    private String comment;
    private Date date;

    public FeedbackDTO() {
    }

    // Constructor đầy đủ
    public FeedbackDTO(int feedbackID, String courseID, String userID, int rating, String comment, Date date) {
        this.feedbackID = feedbackID;
        this.courseID = courseID; // <--- THÊM
        this.userID = userID;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    // Getter Setter
    public int getFeedbackID() { return feedbackID; }
    public void setFeedbackID(int feedbackID) { this.feedbackID = feedbackID; }
    
    public String getCourseID() { return courseID; } // <--- THÊM
    public void setCourseID(String courseID) { this.courseID = courseID; } // <--- THÊM

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
