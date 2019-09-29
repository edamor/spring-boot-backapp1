package com.zuitt.capstone3.Models;

import com.zuitt.capstone3.Repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class ScheduledTask {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Scheduled(fixedRate = 3600000)
    public void appointmentExpScheduler() throws NullPointerException {
        long exp = 172800000;
        Date today = new Date();
        List<Appointment> appointments = appointmentRepository.findAll();

        appointments.forEach(n ->  {
            if (!n.getAccepted()) {
                if ((n.getCreatedAt().getTime() + exp) < today.getTime()) {
                    System.out.println("expired");
                    n.setExpired(true);
                    appointmentRepository.save(n);
                    return;
                }
                System.out.println("di pa expired");;
                return;
            }
            System.out.println(n.getAccepted());
        });

    }
}
