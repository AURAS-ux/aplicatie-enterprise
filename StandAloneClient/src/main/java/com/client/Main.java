package com.client;


import com.google.gson.Gson;
import ejbs.car.Car;
import ejbs.car.CarServiceRemote;
import ejbs.Rental.RentalServiceRemote;
import ejbs.customer.Customer;
import ejbs.customer.CustomerServiceRemote;

import javax.naming.InitialContext;
import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            InitialContext ctx = new InitialContext();
            CustomerServiceRemote customerService = (CustomerServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/CustomerService!ejbs.customer.CustomerServiceRemote");
            List<Customer> customers = customerService.getCustomers(2);
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}



/*
try {
            //Context context = getContext();
            InitialContext ctx = new InitialContext();
            // Lookup the remote EJB
//            PersonServiceRemote service = (PersonServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/PersonService!com.atf.demo.PersonServiceRemote");
//java:global/demo-1.0-SNAPSHOT/CustomerService!ejbs.customer.CustomerServiceRemote
            CustomerServiceRemote service = (CustomerServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/CustomerService!ejbs.customer.CustomerServiceRemote");
            // Call the EJB method
            //service.addCustomer("Auras","E2210LTP9","auras@smth.com","0732111233");
            List<Customer> customers = service.getCustomers(0);
            for (Customer customer : customers) {
                System.out.println(customer);
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

 */