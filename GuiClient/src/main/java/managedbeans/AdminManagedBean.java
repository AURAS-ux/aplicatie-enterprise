package managedbeans;


import com.google.gson.Gson;
import ejbs.Rental.Rental;
import ejbs.Rental.RentalServiceRemote;
import ejbs.car.Car;
import ejbs.car.CarServiceRemote;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named(value = "managedAdmin")
@SessionScoped
public class AdminManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerManagedBean customerManagedBean;

    @EJB
    private CarServiceRemote carService;
    @EJB
    private RentalServiceRemote rentalService;

    private Boolean showCars = false;
    private Boolean showAddCarMenu = false;
    private Boolean showRents = false;

    private List<Rental> rentals;
    private List<Car> cars;
    private String carBrand;
    private String carModel;
    private int carYear;
    private int carPrice;

    public CustomerManagedBean getCustomerManagedBean() {
        return customerManagedBean;
    }
    public Boolean getShowCars() {
        return showCars;
    }

    public void setShowCars(Boolean showCars) {
        this.showCars = showCars;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Boolean getShowAddCarMenu() {
        return showAddCarMenu;
    }

    public void setShowAddCarMenu(Boolean showAddCarMenu) {
        this.showAddCarMenu = showAddCarMenu;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public int getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(int carPrice) {
        this.carPrice = carPrice;
    }

    public Boolean getShowRents() {
        return showRents;
    }

    public void setShowRents(Boolean showRents) {
        this.showRents = showRents;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public AdminManagedBean() {}

    public void showEditTable(){
        showCars = true;
        cars = carService.getAllCars(0);
    }

    public void addCarBtnPressed(){
        showAddCarMenu = true;
    }

    public void addPressed(){
        if(carBrand.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Brand must not be empty"));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("showAlert", true);
        } else if (carYear == 0 || carYear < 1950) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Please select a valid year (YEAR > 1950)"));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("showAlert", true);
        } else if (carModel.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Model is empty ..."));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("showAlert", true);
        } else if (carPrice == 0) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Invalid rent price"));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("showAlert", true);
        }else {
            carService.addCar(this.carBrand, this.carModel, this.carYear, this.carPrice,true);
        }
        showAddCarMenu = false;
    }

    public void editCar(Long id,Car newCar){
        carService.updateCar(id,newCar);
        cars = carService.getAllCars(0);
    }

    public void hideEditMenu(){
        showCars = false;
    }

    public void checkReturnsPressed(){
        this.rentals = rentalService.getRentalInProgress();
        showRents = true;
    }

    public void hideRentsPressed(){
        showRents = false;
    }

    public void excludeCar(Car car){
        Gson gson = new Gson();
        car.setIs_available(false);
        String json = gson.toJson(car);
        carService.updateCar(json);
    }
}
