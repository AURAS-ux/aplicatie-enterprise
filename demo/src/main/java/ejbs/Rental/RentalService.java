package ejbs.Rental;

import com.google.gson.Gson;
import ejbs.car.Car;
import ejbs.customer.Customer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.List;

@Stateless
public class RentalService implements RentalServiceRemote {
    @PersistenceContext(unitName = "defaultTdp")
    private EntityManager em;

    @Override
    public Rental getRental(Long id) {
        return em.find(Rental.class, id);
    }

    @Override
    public List<Rental> getAllRentals(int limit) {
        TypedQuery<Rental> query = em.createNamedQuery("getAllRentals", Rental.class);
        if(limit != 0){
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    @Override
    public List<Rental> getRentalsByCustomer(Long customerId) {
        TypedQuery<Rental> query = em.createNamedQuery("getRentalsByCustomer", Rental.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public void addRental(Long carId, Long customerId, Date rentalDate, Date returnDate,Integer price) {
        Car rentedCar = em.find(Car.class, carId);
        Customer customer = em.find(Customer.class, customerId);
        if(rentedCar == null){
            System.out.println("Rental car not found");
        }else if(customer == null){
            System.out.println("Customer not found");
        }
        Rental rental = new Rental(rentalDate, returnDate, rentedCar, customer,price);
        em.persist(rental);
    }

    @Override
    public void updateRental(String updatedRental) {
        Gson gson = new Gson();
        Rental deserializedRental = gson.fromJson(updatedRental, Rental.class);
        em.merge(deserializedRental);
    }

    @Override
    public void deleteRental(Long rentalId) {
        Rental rental = em.find(Rental.class, rentalId);
        if(rental != null){
            em.remove(rental);
        }else
            throw new RuntimeException("Rental not found");
    }
}
