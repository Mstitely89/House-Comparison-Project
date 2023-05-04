import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class HouseComparison extends Application {

    private ObservableList<House> houses = FXCollections.observableArrayList();

    // Define the loadHouses() method in the Main class
    private void loadHouses() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("houses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String address = parts[0];
                int price = Integer.parseInt(parts[1]);
                int bedrooms = Integer.parseInt(parts[2]);
                int bathrooms = Integer.parseInt(parts[3]);
                House house = new House(address, price, bedrooms, bathrooms);
                houses.add(house);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading houses from file: " + e.getMessage());
        }
    }

    // Define the saveHouses() method in the Main class
    private void saveHouses() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("houses.txt"));
            for (House house : houses) {
                String line = house.getAddress() + "," + house.getPrice() + "," + house.getBedrooms() + "," + house.getBathrooms();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving houses to file: " + e.getMessage());
        }
    }

    private ListView<House> houseListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadHouses();

        houseListView = new ListView<>();
        houseListView.getItems().addAll(houses);

        Button addHouseButton = new Button("Add House");
        addHouseButton.setOnAction(e -> showHouseDialog(null));

        Button editHouseButton = new Button("Edit House");
        editHouseButton.setOnAction(e -> {
            House selectedHouse = houseListView.getSelectionModel().getSelectedItem();
            if (selectedHouse != null) {
                showHouseDialog(selectedHouse);
            }
        });

        Button deleteHouseButton = new Button("Delete House");
        deleteHouseButton.setOnAction(e -> {
            House selectedHouse = houseListView.getSelectionModel().getSelectedItem();
            if (selectedHouse != null) {
                houses.remove(selectedHouse);
                saveHouses();
            }
        });

        VBox buttonsBox = new VBox(10, addHouseButton, editHouseButton, deleteHouseButton);
        buttonsBox.setAlignment(Pos.TOP_CENTER);

        HBox mainBox = new HBox(10, houseListView, buttonsBox);
        mainBox.setPadding(new Insets(10));
        mainBox.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("House Comparator");
        primaryStage.show();
    }

    private void showHouseDialog(House house) {
        Stage stage = new Stage();
        stage.setTitle(house == null ? "Add House" : "Edit House");

        TextField addressTextField = new TextField(house == null ? "" : house.getAddress());
        TextField priceTextField = new TextField(house == null ? "" : Integer.toString(house.getPrice()));
        TextField bedroomsTextField = new TextField(house == null ? "" : Integer.toString(house.getBedrooms()));
        TextField bathroomsTextField = new TextField(house == null ? "" : Integer.toString(house.getBathrooms()));

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(e -> {
            String address = addressTextField.getText().trim();
            int price = Integer.parseInt(priceTextField.getText().trim());
            int bedrooms = Integer.parseInt(bedroomsTextField.getText().trim());
            int bathrooms = Integer.parseInt(bathroomsTextField.getText().trim());

            if (house == null) {
                houses.add(new House(address, price, bedrooms, bathrooms));
            } else {
                house.setAddress(address);
                house.setPrice(price);
                house.setBedrooms(bedrooms);
                house.setBathrooms(bathrooms);
            }

            saveHouses();
            stage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e -> stage.close());

        HBox buttonsBox = new HBox(10, saveButton, cancelButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(10, new Label("Address:"), addressTextField,
                new Label("Price:"), priceTextField,
                new Label("Bedrooms:"), bedroomsTextField,
                new Label("Bathrooms:"), bathroomsTextField,
                buttonsBox);
        mainBox.setPadding(new Insets(10));
        mainBox.setAlignment(Pos.CENTER);

        // Create the ListView for the houses
        ListView<House> houseListView = new ListView<>();
        houseListView.setPrefSize(400, 400);
        houseListView.setItems(houses);

        // Add the ListView to the main VBox
        mainBox.getChildren().addAll(houseListView);//, addButton, editButton, deleteButton);
    }
}