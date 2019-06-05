package com.blackdartq.schoolproject.Utils;

public class Term {
    private int id;
    private String name;
    private String startDate;
    private String endDate;

    public Term(){ }
    public Term(int id, String name, String startDate, String endDate){
        this.setId(id);
        this.setName(name);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
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
}
