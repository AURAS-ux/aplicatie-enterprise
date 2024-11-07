package ejbs.car;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface CarServiceRemote {
    public Car getCar(Long id);
    public List<Car> getAllCars(int limit);
    public void addCar(String brand, String model, int year,Long rentalId);
    public void addCar(String brand, String model, int year);
    public void updateCar(Long carId,Car newCar);
    public void deleteCar(Car car);
}
