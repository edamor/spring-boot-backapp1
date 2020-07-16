package com.zuitt.capstone3.Controllers;

import com.zuitt.capstone3.Models.Appointment;
import com.zuitt.capstone3.Models.User;
import com.zuitt.capstone3.Repositories.AppointmentRepository;
import com.zuitt.capstone3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @GetMapping("/user/{user_id}")
    public User getUser(@PathVariable String user_id) {
        return userRepository.findById(user_id).get();
    }

    @PostMapping("/user/appointments/{user_id}")
    public List<Appointment> getByUserId(@PathVariable String user_id) {
        return appointmentRepository.findByTransactingUsers_Id(user_id);
    }

    @PostMapping("/appointments")
    public List<Appointment> getAvailableAppointments() {
        List<Appointment> returnThis = new ArrayList<>();
        List<Appointment> notAcc = appointmentRepository.findByIsAccepted(false);
        notAcc.forEach(n -> {
            if (!n.getExpired() && !n.getDeleted()) {
                returnThis.add(n);
            }
        });
        return returnThis;
    }

    @PostMapping("/appointments/request/{customer_id}")
    public Appointment requestAppointment(@RequestBody Appointment appointment,
                                         @PathVariable String customer_id) {
        User customer = userRepository.findById(customer_id).get();
            appointment.addTransactingUser(customer);
            Date createdAt = new Date();
            appointment.setCreatedAt(createdAt);
            customer.addAppointment(appointment);
            userRepository.save(customer);
            return appointmentRepository.save(appointment);
    }

    @PutMapping("/appointments/accept/{appointment_id}")
    public User acceptAppointment(@PathVariable String appointment_id,
                                  @RequestBody String user_id) {
        User serviceProvider = userRepository.findById(user_id).get();
        Appointment acceptThis = appointmentRepository.findById(appointment_id).get();
        if(!acceptThis.getAccepted()) {
            acceptThis.setAccepted(true);
            serviceProvider.addAppointment(acceptThis);
            acceptThis.addTransactingUser(serviceProvider);
            Date updatedAt = new Date();
            acceptThis.setUpdatedAt(updatedAt);
            System.out.println(acceptThis.getAccepted());
            appointmentRepository.save(acceptThis);
            return userRepository.save(serviceProvider);
        }
        return serviceProvider;
    }

    @PutMapping("/appointments/customer-feedback/{appointment_id}")
    public Appointment customerFulfilled(@PathVariable String appointment_id) {
        Appointment appointment = appointmentRepository.findById(appointment_id).get();
        appointment.setFulfilled(true);
        Date updatedAt = new Date();
        appointment.setUpdatedAt(updatedAt);
        return appointmentRepository.save(appointment);
    }

    @PutMapping("/appointment/service/{appointment_id}")
    public Appointment editServiceType(@PathVariable String appointment_id,
                                       @RequestBody String service_type) {
        Appointment foundAppointment = appointmentRepository.findById(appointment_id).get();
        if(!foundAppointment.getAccepted()) {
            foundAppointment.setServiceType(service_type);
            return appointmentRepository.save(foundAppointment);
        }
        return foundAppointment;
    }

    @PutMapping("/appointment/address/{appointment_id}")
    public Appointment editAddress(@PathVariable String appointment_id,
                                       @RequestBody String address) {
        Appointment foundAppointment = appointmentRepository.findById(appointment_id).get();
        if(!foundAppointment.getAccepted()) {
            foundAppointment.setAppointmentLocation(address);
            return appointmentRepository.save(foundAppointment);
        }
        return foundAppointment;
    }


    @DeleteMapping("/appointments/{appointment_id}")
    public String deleteAppointment(@PathVariable String appointment_id) {
        Appointment deleteThisAppointment = appointmentRepository.findById(appointment_id).get();
        if(!deleteThisAppointment.getAccepted()) {
            if (deleteThisAppointment.getTransactingUsers().size() <= 1) {
                User user1 = userRepository.findById(deleteThisAppointment.getTransactingUsers().get(0).getId()).get();
                deleteThisAppointment.setDeleted(true);
                appointmentRepository.save(deleteThisAppointment);
                user1.getAppointments().remove(deleteThisAppointment);
                userRepository.save(user1);
                return "Appointment Removed";
            }
        }
        return "failed";
    }

    @DeleteMapping("/user/delete/{user_id}")
    public void deleteUserById(@PathVariable String user_id) {
        User deleteThisUser = userRepository.findById(user_id).get();
        userRepository.delete(deleteThisUser);
    }




}
