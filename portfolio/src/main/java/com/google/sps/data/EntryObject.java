package com.google.sps.data;

import java.sql.Timestamp;    
import java.util.Date;

public class EntryObject {
    Integer year;
    Integer month;
    Integer day;
    Integer deaths;
    Integer cases;

    public EntryObject(Integer newYear, Integer newMonth, Integer newDay, 
        Integer numCases, Integer numDeaths) {
        year = newYear;
        month = newMonth;
        day = newDay;
        deaths = numDeaths;
        cases = numCases;
    }
    
    public Integer getYear(){
        return year;
    }

    public Integer getMonth(){
        return month;
    }
    
    public Integer getDay(){
        return day;
    }

    public Integer getCases(){
        return deaths;
    }

    public Integer getDeaths(){
        return cases;
    }
}
