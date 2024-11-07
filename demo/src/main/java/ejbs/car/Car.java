package ejbs.car;

import ejbs.Rental.Rental;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "Car")
public class Car implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private Long car_id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_id", nullable = true)
    private transient Rental rental;

    public Car( String brand, String model, Integer year, Rental rental) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.rental = rental;
    }

    public Car( String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public Car() {

    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCar_id(Long carId) {
        this.car_id = carId;
    }

    public Long getCar_id() {
        return car_id;
    }

    @Override
    public String toString() {
        return "Car{" +
                "car_id=" + car_id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", rental=" + rental +
                '}';
    }
}
