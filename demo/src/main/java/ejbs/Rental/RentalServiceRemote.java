package ejbs.Rental;

import jakarta.ejb.Remote;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Remote
public interface RentalServiceRemote {
    public Rental getRental(Long id);
    public List<Rental> getAllRentals(int limit);
    public void addRental(Long carId, Long customerId, Date rentalDate, Date returnDate);
    public void updateRental(Long rentalId);
    public void deleteRental(Long rentalId);
}
