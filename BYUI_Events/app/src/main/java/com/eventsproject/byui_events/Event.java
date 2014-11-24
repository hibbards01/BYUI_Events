package com.eventsproject.byui_events;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * EVENT
 *  This will hold one event!
 * Created by SamuelHibbard on 11/23/14.
 */
public class Event {
    /*
     * MEMBER VARIABLES
     */
    private String event_id;
    private String name;
    private Date date;
    private String start_time;
    private String end_time;
    private String description;
    private String category;
    private String location;
    //private Blob picture;

    /*
     * MEMBER METHODS
     */

    /**
     * CONSTRUCTOR
     * @param id
     * @param name
     * @param date
     * @param start_time
     * @param end_time
     * @param description
     * @param category
     * @param location
     */
    public Event(String id, String name, String date, String start_time, String end_time,
                 String description, String category, String location) {
        //now construct it!
        this.event_id = id;
        this.name = name;
        //make sure this works!
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.category = category;
        this.location = location;
    }

    /*
     * GETTERS
     */
    public String getEvent_id() {
        return event_id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }
}
