package ejbs.Rental;

import ejbs.car.Car;
import ejbs.customer.Customer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.time.LocalDate;
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
        TypedQuery<Rental> query = em.createQuery("SELECT r FROM Rental r", Rental.class);
        if(limit != 0){
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    @Override
    public void addRental(Long carId, Long customerId, Date rentalDate, Date returnDate) {
        Car rentedCar = em.find(Car.class, carId);
        Customer customer = em.find(Customer.class, customerId);
        if(rentedCar == null){
            System.out.println("Rental car not found");
        }else if(customer == null){
            System.out.println("Customer not found");
        }
        Rental rental = new Rental(rentalDate, returnDate, rentedCar, customer);
        em.persist(rental);
    }

    @Override
    public void updateRental(Long rentalId) {

    }

    @Override
    public void deleteRental(Long rentalId) {

    }
}
