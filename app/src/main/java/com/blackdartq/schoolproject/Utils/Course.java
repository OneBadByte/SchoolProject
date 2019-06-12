package com.blackdartq.schoolproject.Utils;

public class Course {
    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private String status;
    private String mentorNames;
    private String phoneNumber;
    private String email;
    private Integer termId;


    public Course(){ }
    public Course(
            int id, String name, String startDate,
            String endDate, String status, String mentorName,
            String phoneNumber, String email, int termId){
        this.setId(id);
        this.setName(name);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStatus(status);
        this.setMentorNames(mentorName);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.setTermId(termId);
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMentorNames() {
        return mentorNames;
    }

    public void setMentorNames(String mentorNames) {
        this.mentorNames = mentorNames;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
