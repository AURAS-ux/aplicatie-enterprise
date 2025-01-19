package ejbs.Rental;

import ejbs.car.Car;
import ejbs.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import status.Status;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "Rental")
@NamedQuery(name = "getAllRentals",query = "select r from Rental r")
@NamedQuery(name = "getRentalsByCustomer",query = "select rent from Rental rent where rent.customer.customer_id = :customerId")
@NamedQuery(name = "getRentalsInProgress",query = "select rent from Rental rent where rent.status = status.Status.IN_PROGRESS")
public class Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rental_id;

    @NotNull
    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;

    @NotNull
    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull
    @Column(name = "rental_price", nullable = false)
    private Integer rentalPrice;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Integer getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(Integer rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Rental(Date rentalDate, Date returnDate, Car car, Customer customer, Integer rentalPrice, Status status) {
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.car = car;
        this.customer = customer;
        this.rentalPrice = rentalPrice;
        this.status = status;
    }

    public Rental() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setRental_id(Long rentalId) {
        this.rental_id = rentalId;
    }

    public Long getRental_id() {
        return rental_id;
    }

    public @NotNull Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rental_id=" + rental_id +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", car=" + car +
                ", customer=" + customer +
                ", rentalPrice=" + rentalPrice +
                ", status=" + status +
                '}';
    }
}
