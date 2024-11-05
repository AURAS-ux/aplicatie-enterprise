package com.client;


import com.atf.demo.PersonServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Main {
    public static void main(String[] args) {
        try {
            //Context context = getContext();
            InitialContext ctx = new InitialContext();
            // Lookup the remote EJB
            PersonServiceRemote service = (PersonServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/PersonService!com.atf.demo.PersonServiceRemote");

            // Call the EJB method
            service.addPerson("Auras Paltanea1105");
            System.out.println("Person added successfully.");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
