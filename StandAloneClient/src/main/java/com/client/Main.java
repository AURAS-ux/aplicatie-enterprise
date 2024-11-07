package com.client;


import com.atf.demo.PersonServiceRemote;
import ejbs.Rental.Rental;
import ejbs.Rental.RentalServiceRemote;
import ejbs.car.Car;
import ejbs.car.CarServiceRemote;
import ejbs.customer.Customer;
import ejbs.customer.CustomerServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try {
            InitialContext ctx = new InitialContext();
            CarServiceRemote service = (CarServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/CarService!ejbs.car.CarServiceRemote");
            RentalServiceRemote rentalService = (RentalServiceRemote) ctx.lookup("java:global/demo-1.0-SNAPSHOT/RentalService!ejbs.Rental.RentalServiceRemote");
            Rental carRental = rentalService.getRental(1L);
            System.out.println(carRental);
            Car newCar = new Car("Audi","A4",2014,carRental);
            System.out.println(newCar);
            service.updateCar(1L,newCar);
            System.out.println("Car added successfully");

//
//            LocalDate startDate = LocalDate.of(2020, 12, 1);
//            LocalDate endDate = LocalDate.of(2020, 12, 20);
//
//            Date sqlStartDate = Date.valueOf(startDate);
//            Date sqlEndDate = Date.valueOf(endDate);
//            service.addRental(1L,1L,sqlStartDate,sqlEndDate);
//            System.out.println("Rentals added");
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