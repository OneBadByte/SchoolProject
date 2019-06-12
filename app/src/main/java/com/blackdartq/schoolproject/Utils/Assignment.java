package com.blackdartq.schoolproject.Utils;

public class Assignment {
    private Integer id;
    private String name;
    private String optionalNote;
    private String dueDate;
    private Integer courseId;

    public Assignment(Integer id, String name, String optionalNote, String dueDate) {
        this.id = id;
        this.name = name;
        this.optionalNote = optionalNote;
        this.dueDate = dueDate;
    }

    public Assignment(Integer id, String name, String optionalNote, String dueDate, Integer courseId) {
        this.id = id;
        this.name = name;
        this.optionalNote = optionalNote;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    public Assignment() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptionalNote() {
        return optionalNote;
    }

    public void setOptionalNote(String optionalNote) {
        this.optionalNote = optionalNote;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
