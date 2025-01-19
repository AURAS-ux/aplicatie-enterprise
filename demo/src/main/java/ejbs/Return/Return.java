package ejbs.Return;

import ejbs.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "return")
@NamedQuery(name = "getAllReturns",query = "select r from Return r")
@NamedQuery(name = "getReturnsByCustomer",query = "select r from Return r where r.customer.customer_id = :customerId")
public class Return implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "returnid")
    private int returnId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerid",nullable = false)
    private Customer customer;

    @NotNull
    @Column(name = "returned")
    private boolean returned;

    public Return(int returnId, Customer customer, boolean returned) {
        this.returnId = returnId;
        this.customer = customer;
        this.returned = returned;
    }

    public Return(){}

    @NotNull
    public boolean isReturned() {
        return returned;
    }

    public void setReturned(@NotNull boolean returned) {
        this.returned = returned;
    }

    public @NotNull Customer getCustomer() {
        return customer;
    }

    public void setCustomer(@NotNull Customer customer) {
        this.customer = customer;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Return aReturn = (Return) o;
        return returnId == aReturn.returnId && returned == aReturn.returned && Objects.equals(customer, aReturn.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnId, customer, returned);
    }

    @Override
    public String toString() {
        return "Return{" +
                "returnId=" + returnId +
                ", customer=" + customer +
                ", returned=" + returned +
                '}';
    }
}
