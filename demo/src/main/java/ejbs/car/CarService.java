package ejbs.car;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CarService implements CarServiceRemote {
    @PersistenceContext(unitName = "defaultTdp")
    private EntityManager em;

    @Override
    public Car getCar(Long id) {
        System.out.println("Looking for Car with ID:"+id);
        return em.find(Car.class, id);
    }

    @Override
    public List<Car> getAllCars(int limit) {
        TypedQuery<Car> query = em.createNamedQuery("getAllCars", Car.class);
        if (limit != 0) {
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    @Override
    public List<String> getAllAvailableCars() {
        TypedQuery<Car> query = em.createNamedQuery("getAvailableCars", Car.class);
        List<Car> list =  query.getResultList();
        List<String> availableCars = new ArrayList<String>();

        for (Car car : list) {
            Gson gson = new Gson();
            availableCars.add(gson.toJson(car));
        }

        return availableCars;
    }

    @Override
    public void addCar(String brand, String model, int year,int price) {
        Car newCar = new Car(brand, model, year,price);
        em.persist(newCar);
    }

    @Override
    public void updateCar(Long id, Car newCar) {
        System.out.println("Received Car: " + newCar);
        em.merge(newCar);
    }

    @Override
    public void updateCar(String newCar) {
        Gson gson = new Gson();
        Car deserializedCar = gson.fromJson(newCar,Car.class);
        em.merge(deserializedCar);
    }

    @Override
    public void deleteCar(Long carId) {
        Car car = em.find(Car.class, carId);
        if (car != null) {
            em.remove(car);
        }else
            throw new RuntimeException("Car not found");
    }
}
