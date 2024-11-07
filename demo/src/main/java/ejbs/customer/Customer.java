package ejbs.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="customer")
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    @NotNull
    @Lob
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Lob
    @Column(name = "licence_nr", nullable = false)
    private String licenceNr;

    @NotNull
    @Lob
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Lob
    @Column(name = "phone", nullable = false)
    private String phone;

    public Customer(){}
    public Customer(String name, String licenceNr, String email, String phone) {
        this.name = name;
        this.licenceNr = licenceNr;
        this.email = email;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenceNr() {
        return licenceNr;
    }

    public void setLicenceNr(String licenceNr) {
        this.licenceNr = licenceNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCustomer_id(Long customerId) {
        this.customer_id = customerId;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                ", name='" + name + '\'' +
                ", licenceNr='" + licenceNr + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
