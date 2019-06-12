package com.blackdartq.schoolproject.Utils;

import java.util.ArrayList;

public class Term {
    private Integer id;
    private String name;
    private String startDate;
    private String endDate;
    private ArrayList<Integer> courseIds;

    public Term(){
        courseIds = new ArrayList<>();
    }
    public Term(int id, String name, String startDate, String endDate){
        this.setId(id);
        this.setName(name);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        courseIds = new ArrayList<>();
    }

    public void appendCourseIdToTerm(int id){
        courseIds.add(id);
    }

    public int getCourseIdFromTerm(int index){
        return courseIds.get(index);
    }

    public ArrayList<Integer> getCourseIdsFromTerm(){
        return courseIds;
    }

    public void removeCourseIdFromTermByIndex(int index){
        courseIds.remove(index);
    }

    public void removeCourseIdFromTermByCourseId(int id){
        int index = 0;
        for(int i : courseIds){
            if(i == id){
                break;
            }
            index++;
        }
        courseIds.remove(index);
    }

    public void addCourseIdsToTerm(ArrayList<Integer> ids){
        courseIds = ids;
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
}
