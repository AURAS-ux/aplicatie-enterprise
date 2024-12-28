package managedbeans;

import com.google.gson.Gson;
import ejbs.customer.Customer;
import ejbs.customer.CustomerServiceRemote;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named(value = "managedCustomer")
@SessionScoped
public class CustomerManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private CustomerServiceRemote customerService;

    private Long customerId;
    private String name;
    private String phone;
    private String email;
    private String licenseId;
    private Boolean isAdmin;
    private Customer customer;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }



    public CustomerManagedBean() {

        System.out.println("Initializing... CustomerManagedBean");
    }

    @PostConstruct
    public void finalInit(){
        System.out.println("Initialized CustomerManagedBean");
        if (customerService == null) {
            System.out.println("Found null remote interface");
        } else {
            System.out.println("Remote interface found");
        }
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for licenseId
    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
    public String login() {
        System.out.println("Logging in attempted with values " + email + " " + licenseId);
        try {
            boolean hasAccount = customerService.getCustomerByEmailAndLicense(email, licenseId);
            if(hasAccount){
                String data = customerService.getCustomerData(email, licenseId);
                Customer customer = new Gson().fromJson(data, Customer.class);
                this.customer = customer;
                this.name = customer.getName();
                this.phone = customer.getPhone();
                this.customerId = customer.getCustomer_id();
                this.isAdmin = customer.getIsAdmin();

                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("role", isAdmin ? "admin" : "user");

                if(isAdmin)
                    return "adminPage?faces-redirect=true";
                return "succes?faces-redirect=true";
            }else{
                return "noaccount?faces-redirect=true";
            }
//            return hasAccount ? "succes?faces-redirect=true" : "noaccount?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Failed to access CustomerServiceRemote: " + e.getMessage());
            return null;
        }
    }

    public String SignIn(){
        customerService.addCustomer(name,licenseId,email,phone);
        System.out.println("Sign in attempted with values " + email + " " + licenseId);
        return "index?faces-redirect=true";
    }

    public void editUser(){
        customer.setName(name);
        customer.setPhone(phone);
        customer.setLicenceNr(licenseId);
        customer.setEmail(email);
        customerService.updateCustomer(customer);
    }
}
