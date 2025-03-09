package com.distribuida.correo.modelo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {

    private Emailinfo fromAddress;
    private Emailinfo toAddress;
    private String subject;
    private String emailBody;

}
