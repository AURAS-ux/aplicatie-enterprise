package ejbs.customer;

import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Stateless
public class CustomerService implements CustomerServiceRemote {
    @PersistenceContext(unitName = "defaultTdp")
    private EntityManager em;

    @Override
    public Customer getCustomer(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    public boolean getCustomerByEmailAndLicense(String email, String licenseId) {
        try{
            Query query = em.createNamedQuery("findByEmailAndLicense");
            query.setParameter("email", email);
            query.setParameter("licenseId", licenseId);
            if (query.getSingleResult() != null)
                return true;
            else
                return false;
        }catch (NoResultException noResultException){
            return false;
        }
    }

    @Override
    public String getCustomerData(String email, String licenseId) {
        if(getCustomerByEmailAndLicense(email, licenseId)){
            Gson gson = new Gson();
            Query query = em.createNamedQuery("findByEmailAndLicense");
            query.setParameter("email", email);
            query.setParameter("licenseId", licenseId);
            Customer data = (Customer) query.getSingleResult();
            return gson.toJson(data);
        }else{
            return null;
        }
    }

    @Override
    public List<Customer> getCustomers(int limit) {
        TypedQuery<Customer> query = em.createNamedQuery("getAllCustomers", Customer.class);
        if(limit != 0){
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    @Override
    public void addCustomer(String name, String licenseNr, String email, String phone) {
       Customer customer = new Customer(name, licenseNr, email, phone);
       em.persist(customer);
    }

    @Override
    public void updateCustomer(String jsonSerializedCustomer) {
        Gson gson = new Gson();
        Customer receivedCustomer = gson.fromJson(jsonSerializedCustomer, Customer.class);
        em.merge(receivedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = em.find(Customer.class, id);
        if(customer != null){
            em.remove(customer);
        }else
            throw new RuntimeException("Customer not found");
    }
}
