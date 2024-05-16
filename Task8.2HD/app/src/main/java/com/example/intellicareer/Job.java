package com.example.intellicareer;

import java.util.List;

public class Job {
    private String jobId;
    private String title;
    private String skills;
    private String minSalary;
    private String maxSalary;
    private String description;
    private String date;
    private String iconUrl;
    private Boolean whetherShowSuitability;
    private int suitAbility;
    private String employerId;
    private List<String> employeeIdList;

    public String getTitle() {
        return title;
    }

    public Job() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(String minSalary) {
        this.minSalary = minSalary;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(String maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Boolean getWhetherShowSuitability() {
        return whetherShowSuitability;
    }

    public void setWhetherShowSuitability(Boolean whetherShowSuitability) {
        this.whetherShowSuitability = whetherShowSuitability;
    }

    public int getSuitAbility() {
        return suitAbility;
    }

    public void setSuitAbility(int suitAbility) {
        this.suitAbility = suitAbility;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public List<String> getEmployeeIdList() {
        return employeeIdList;
    }

    public void setEmployeeIdList(List<String> employeeIdList) {
        this.employeeIdList = employeeIdList;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Job(String jobId, String title, String skills, String minSalary, String maxSalary, String description, String date, String iconUrl, Boolean whetherShowSuitability, int suitAbility, String employerId, List<String> employeeIdList) {
        this.jobId = jobId;
        this.title = title;
        this.skills = skills;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.description = description;
        this.date = date;
        this.iconUrl = iconUrl;
        this.whetherShowSuitability = whetherShowSuitability;
        this.suitAbility = suitAbility;
        this.employerId = employerId;
        this.employeeIdList = employeeIdList;
    }
}
