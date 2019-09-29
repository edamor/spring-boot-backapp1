package com.zuitt.capstone3.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "appointments")
public class Appointment {

    private String id = UUID.randomUUID().toString();
    private String serviceType;
    private String appointmentDate;
    private String timeSlot;
    private String appointmentLocation;
    private Boolean isAccepted = false;
    private Boolean isFulfilled = false;
    private Boolean isExpired = false;
    private Date createdAt;
    private Date updatedAt;

    @DBRef(lazy = true)
    @JsonBackReference
    private List<User> transactingUsers = new ArrayList<>(2);

    public Appointment() {
    }

    public Appointment(String serviceType, String appointmentDate, String timeSlot, String appointmentLocation) {
        this.serviceType = serviceType;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.appointmentLocation = appointmentLocation;
    }

    public String getId() {
        return id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public Boolean getFulfilled() {
        return isFulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getTransactingUsers() {
        return transactingUsers;
    }

    public void setTransactingUsers(List<User> transactingUsers) {
        this.transactingUsers = transactingUsers;
    }

    public void addTransactingUser(User user) {
        this.transactingUsers.add(user);
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }
}
