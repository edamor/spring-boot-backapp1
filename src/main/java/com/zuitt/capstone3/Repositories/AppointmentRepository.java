package com.zuitt.capstone3.Repositories;

import com.zuitt.capstone3.Models.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByTransactingUsers_Id(String id);
    List<Appointment> findByIsAccepted(Boolean isAccepted);
}
