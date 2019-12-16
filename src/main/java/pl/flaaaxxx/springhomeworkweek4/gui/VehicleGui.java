package pl.flaaaxxx.springhomeworkweek4.gui;

import com.helger.css.decl.CSSMediaQuery;
import com.sun.glass.ui.View;
import com.sun.java.accessibility.util.GUIInitializedListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.flaaaxxx.springhomeworkweek4.manager.VehicleManager;
import pl.flaaaxxx.springhomeworkweek4.model.Vehicle;

import java.util.Optional;

@Route("show-cars")
public class VehicleGui extends VerticalLayout {

    private VehicleManager vehicleManager;

    @Autowired
    public VehicleGui(VehicleManager vehicleManager) {
        this.vehicleManager = vehicleManager;

        Grid<Vehicle> grid = new Grid<>(Vehicle.class);
        grid.setHeight("300px");

        grid.addColumn(new ComponentRenderer<>((vehicle) -> {
            return new Button("Delete", clickEvent -> {
                vehicleManager.deleteVehicle(
                        this.vehicleManager.getVehicleList().stream().filter(v -> v.getId() == vehicle.getId()
                        ).findFirst().get().getId()
                );
                grid.setItems(this.vehicleManager.getVehicleList());
            });
        })).setHeader("Delete");


//        add vehicle
        TextField textFieldId = new TextField("Id - fill in to add");
        TextField textFieldMark = new TextField("Mark - fill in to add or modify");
        TextField textFieldModel = new TextField("Model - fill in to add or modify");
        TextField textFieldColor = new TextField("Color - Fill in to add or modify");

        Button buttonAdd = new Button("Add car");
        buttonAdd.addClickListener(clickEvent -> {
            Dialog dialog = new Dialog();
            if (!textFieldId.getValue().equals("")) {

                Optional<Vehicle> v = this.vehicleManager.getElementById(Long.parseLong(textFieldId.getValue()));
                if (!v.isPresent()) {
                    Vehicle vehicle = new Vehicle(Long.parseLong(textFieldId.getValue()), textFieldMark.getValue(), textFieldModel.getValue(), textFieldColor.getValue());
                    vehicleManager.addvehicle(vehicle);
                } else {
                    dialog.add("Pojazd o podanym Id istnieje już w bazie.");
                    dialog.open();
                }
            } else {
                dialog.add("Pole ID nie może być puste. Id musi być liczbą.");
                dialog.open();
            }


            grid.setItems(this.vehicleManager.getVehicleList());
        });

//        modification vehicle
        grid.addColumn(new ComponentRenderer<>((vehicle) -> {
            return new Button("Modify", clickEvent -> {
                Vehicle v = new Vehicle(vehicle.getId(), textFieldMark.getValue(), textFieldModel.getValue(), textFieldColor.getValue());
                if (textFieldMark.getValue().equals(""))
                    v.setMark(vehicle.getMark());
                if (textFieldModel.getValue().equals(""))
                    v.setModel(vehicle.getModel());
                if (textFieldColor.getValue().equals(""))
                    v.setColor(vehicle.getColor());

                vehicleManager.modVehicle(v);
                grid.setItems(this.vehicleManager.getVehicleList());
            });
        })).setHeader("Modify");


//        search vehicle by color
        TextField textFieldSearch = new TextField("Search");
        Button buttonColor = new Button("Search color");
        buttonColor.addClickListener(clickEvent -> {
            if (!textFieldSearch.getValue().equals(""))
                grid.setItems(this.vehicleManager.getElementByColor(textFieldSearch.getValue()));
            else
                grid.setItems(this.vehicleManager.getVehicleList());
        });

        grid.setItems(this.vehicleManager.getVehicleList());
        add(textFieldSearch, buttonColor);
        add(grid, textFieldId, textFieldMark, textFieldModel, textFieldColor, buttonAdd);
    }
}
