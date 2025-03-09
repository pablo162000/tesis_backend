package com.distribuida.correo.controller;


import com.distribuida.correo.modelo.EmailDetails;
import com.distribuida.correo.service.SendGridEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/correosendgrid")
public class EmailRestController {

    @Autowired
    private SendGridEmailService sendEmailService;

    @PostMapping(value = "/sendSingleEmail")
    public String sendSingleEmail(@RequestBody EmailDetails emailDetails) throws IOException {

        return sendEmailService.sendEmail(emailDetails);

    }

}
