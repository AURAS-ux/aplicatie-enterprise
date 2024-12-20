package ejbs.customer;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface CustomerServiceRemote {
    public Customer getCustomer(Long id);
    public boolean getCustomerByEmailAndLicense(String email, String licenseId);
    public String getCustomerData(String email, String licenseId);
    public List<Customer> getCustomers(int limit);
    public void addCustomer(String name,String licenseNr,String email,String phone);
    public void updateCustomer(String jsonSerializedCustomer);
    public void deleteCustomer(Long id);
}
