package ejbs.car;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Car")
@NamedQuery(name = "getAllCars",query = "select c from Car c")
@NamedQuery(name = "getAvailableCars",query = "SELECT c FROM Car c WHERE c.car_id NOT IN (SELECT r.car.car_id FROM Rental r)")
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


    public Car( String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public Car() {

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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(car_id, car.car_id) && Objects.equals(brand, car.brand) && Objects.equals(model, car.model) && Objects.equals(year, car.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car_id, brand, model, year);
    }
}
