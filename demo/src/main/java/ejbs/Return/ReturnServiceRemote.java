package ejbs.Return;

import jakarta.ejb.Remote;
import java.sql.Date;
import java.util.List;

@Remote
public interface ReturnServiceRemote {
    void addReturn(Long rentalId, Long customerId, Date returnDate, String reason);
    Return getReturn(Long returnId);
    List<Return> getAllReturns(int limit);
    List<Return> getReturnsByCustomer(Long customerId);
    void updateReturn(Long returnId, String updatedDetails);
    void deleteReturn(Long returnId);
}
