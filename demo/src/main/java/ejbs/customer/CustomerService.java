package ejbs.customer;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

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
    public List<Customer> getCustomers(int limit) {
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
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
    public void updateCustomer(Long id, String name, String licenseNr, String email, String phone) {

    }

    @Override
    public void deleteCustomer(Long id) {

    }
}
