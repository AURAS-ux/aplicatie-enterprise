package managedbeans;

import com.google.gson.Gson;
import ejbs.Rental.Rental;
import ejbs.Rental.RentalServiceRemote;
import ejbs.car.Car;
import ejbs.car.CarServiceRemote;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Named(value = "managedRent")
@SessionScoped
public class RentManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerManagedBean customerManagedBean;

    @EJB
    private CarServiceRemote carService;
    @EJB
    private RentalServiceRemote rentService;

    private Gson gson;
    private List<Car> availableCars;
    private Map<Long,Boolean> selectedCarIds;

    private List<Rental> customerRentals;

    private Map<Long,String> rentDays;

    public void setRentDays(Map<Long,String> rentDays) {
        this.rentDays = rentDays;
    }

    public Map<Long,String> getRentDays() {
        return rentDays;
    }

    public void setCustomerManagedBean(CustomerManagedBean customerManagedBean) {
        this.customerManagedBean = customerManagedBean;
    }

    public List<Rental> getCustomerRentals() {
        return customerRentals;
    }
    public List<Car> getAvailableCars() {
        return availableCars;
    }

    public void setAvailableCars(List<Car> availableCars) {
        this.availableCars = availableCars;
    }

    public Map<Long,Boolean> getSelectedCarIds() {
        return selectedCarIds;
    }

    public void setSelectedCarIds(Map<Long,Boolean> selectedCarIds) {
        this.selectedCarIds = selectedCarIds;
    }


    @PostConstruct
    public void init() {
        this.rentDays = new HashMap<>();
        this.gson = new Gson();
        this.availableCars = new ArrayList<>();
        this.selectedCarIds = new HashMap<>();
        List<String> rawAvailableCars = carService.getAllAvailableCars();
        for(String rawCarId : rawAvailableCars) {
            Car car = gson.fromJson(rawCarId, Car.class);
            if(car.getIs_available()) {
                availableCars.add(car);
            }
        }
    }

    public String onNewRent(){
        return "newRent?faces-redirect=true";
    }



    public String rent(){
        if(selectedCarIds.isEmpty() || !selectedCarIds.containsValue(true)) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","You must select at least one car before renting."));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("showAlert", true);

            return null;
        }
        List<Long> selectedItems = selectedCarIds.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());



        for(Long selectedItem : selectedItems) {
            String days = rentDays.getOrDefault(selectedItem,"0");
            if (days == null || Integer.parseInt(days) <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please specify a valid number of rental days for all selected cars."));
                return null;
            }

            Car car = availableCars.stream()
                    .filter(c -> c.getCar_id().equals(selectedItem))
                    .findFirst()
                    .orElse(null);

            if (car == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selected car not found."));
                return null;
            }

            // Calculate total price
            int totalPrice = car.getPrice() * Integer.parseInt(days);


            rentService.addRental(selectedItem,customerManagedBean.getCustomerId(), Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now().plusDays(Integer.parseInt(days))),totalPrice);
        }
        refreshAvailableCars();
        return "succes?faces-redirect=true";
    }

    public String onShowRents(){
        customerRentals = rentService.getRentalsByCustomer(customerManagedBean.getCustomerId());
        return "showRents?faces-redirect=true";
    }

    public void RefreshRents(){
        customerRentals = rentService.getRentalsByCustomer(customerManagedBean.getCustomerId());
    }

    public void refreshAvailableCars(){
        availableCars.clear();
        List<String> rawAvailableCars = carService.getAllAvailableCars();
        for(String rawCarId : rawAvailableCars) {
            Car car = gson.fromJson(rawCarId, Car.class);
            if(car.getIs_available()) {
                availableCars.add(car);
            }
        }
    }
}
