package ejbs.car;

import ejbs.Rental.Rental;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CarService implements CarServiceRemote{
    @PersistenceContext(unitName = "defaultTdp")
    private EntityManager em;
    @Override
    public Car getCar(Long id) {
        return em.find(Car.class, id);
    }

    @Override
    public List<Car> getAllCars(int limit) {
        TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c", Car.class);
        if(limit != 0){
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    @Override
    public void addCar(String brand, String model, int year,Long rentalId) {
        Rental rental = em.find(Rental.class, rentalId);
        if(rental == null){}
        Car newCar = new Car(brand,model,year,rental);
        em.persist(newCar);
    }

    @Override
    public void addCar(String brand, String model, int year) {
        Car newCar = new Car(brand,model,year);
        em.persist(newCar);
    }

    @Override
    public void updateCar(Long carId,Car newCar) {
        Car oldCar = em.find(Car.class, carId);
        System.out.println("Found Car:"+oldCar);
        System.out.println("Received Car"+newCar);
        oldCar.setBrand(newCar.getBrand());
        oldCar.setModel(newCar.getModel());
        oldCar.setYear(newCar.getYear());
        oldCar.setRental(newCar.getRental());
        System.out.println("Updated Car:"+oldCar);
        em.merge(oldCar);
    }

    @Override
    public void deleteCar(Car car) {
        //TODO finish implementation
    }
}
