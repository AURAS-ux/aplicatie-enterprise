package ejbs.Rental;

import jakarta.ejb.Remote;

import java.sql.Date;
import java.util.List;

@Remote
public interface RentalServiceRemote {
    public Rental getRental(Long id);
    public List<Rental> getAllRentals(int limit);
    public List<Rental> getRentalsByCustomer(Long customerId);
    public void addRental(Long carId, Long customerId, Date rentalDate, Date returnDate,Integer price);
    public void updateRental(String updatedRental);
    public void deleteRental(Long rentalId);
    public List<Rental> getRentalInProgress();
}
