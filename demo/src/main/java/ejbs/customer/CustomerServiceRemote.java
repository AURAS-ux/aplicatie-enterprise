package ejbs.customer;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface CustomerServiceRemote {
    public Customer getCustomer(Long id);
    public List<Customer> getCustomers(int limit);
    public void addCustomer(String name,String licenseNr,String email,String phone);
    public void updateCustomer(Long id, String name, String licenseNr, String email, String phone);
    public void deleteCustomer(Long id);
}
