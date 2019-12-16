package pl.flaaaxxx.springhomeworkweek4.manager;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.flaaaxxx.springhomeworkweek4.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleManager {

    private List<Vehicle> vehicleList;

    public VehicleManager() {
        this.vehicleList = new ArrayList<>();
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public void addvehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    public Optional<Vehicle> getElementById(Long id) {
        Optional<Vehicle> v = vehicleList.stream().filter(vehicle -> vehicle.getId() == id).findFirst();
        return v;
    }

    public List<Vehicle> getElementByColor(String color) {
        List<Vehicle> v = vehicleList.stream().filter(vehicle -> vehicle.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
        return v;
    }

    public boolean addVehicleToList(Vehicle vehicle) {
        return vehicleList.add(vehicle);
    }

    public boolean deleteVehicle(Long id) {
        Optional<Vehicle> vehicle = getElementById(id);
        if (vehicle.isPresent()) {
            return vehicleList.remove(vehicle.get());
        }
        return false;
    }

    public void modVehicle(Vehicle newVehicle) {
        Optional<Vehicle> vehicle = getElementById(newVehicle.getId());
        if (vehicle.isPresent()) {
            getVehicleList().set(getVehicleList().indexOf(vehicle.get()), newVehicle);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addSimpVehicle() {
        vehicleList.add(new Vehicle(1L, "Audi", "A6", "Red"));
        vehicleList.add(new Vehicle(2L, "BMW", "E46", "Green"));
        vehicleList.add(new Vehicle(3L, "Pagani Zonda", "C12S", "Blue"));
    }
}
