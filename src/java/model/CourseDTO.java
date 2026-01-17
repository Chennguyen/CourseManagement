/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Lenovo
 */
public class CourseDTO {
    private String id;
    private String name;
    private String description;
    private int duration;
    private float fee;
    private String lecturer; // Mới
    private String roadmap;  // Mới

    public CourseDTO(String id, String name, String description, int duration, float fee, String lecturer, String roadmap) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.fee = fee;
        this.lecturer = lecturer;
        this.roadmap = roadmap;
    }

    public CourseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setCourseName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
    
    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }
    public String getRoadmap() { return roadmap; }
    public void setRoadmap(String roadmap) { this.roadmap = roadmap; }
    
}
