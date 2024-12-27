package ejbs.car;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface CarServiceRemote {
    public Car getCar(Long id);
    public List<Car> getAllCars(int limit);
    public List<String> getAllAvailableCars();
    public void addCar(String brand, String model, int year,int price);
    public void updateCar(Car newCar);
    public void updateCar(String newCar);
    public void deleteCar(Long carId);
}
