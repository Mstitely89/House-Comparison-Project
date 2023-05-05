import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class HouseComparison extends Application {

    private static Deque<DwellingInfo> dwellingsDeque = new LinkedList<>();

    // Define loadHouses()
    private static void loadHouses() {
        File file = new File("dwellings.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 6) {
                    System.err.println("Invalid format on line " + lineNumber + ": expected 6 fields, found " + parts.length);
                } else {
                    try {
                        String dwellingType = parts[0];
                        String address = parts[1];
                        double priceOrRent = Double.parseDouble(parts[2]);
                        int bedrooms = Integer.parseInt(parts[3]);
                        double bathrooms = Double.parseDouble(parts[4]);

                        if (dwellingType.equalsIgnoreCase("House")) {
                            dwellingsDeque.add(new House(address, priceOrRent, bedrooms, bathrooms));
                        } else if (dwellingType.equalsIgnoreCase("Apartment")) {
                            dwellingsDeque.add(new Apartment(address, priceOrRent, bedrooms, bathrooms));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format on line " + lineNumber + ": " + e.getMessage());
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found, starting with an empty list of dwellings.");
        } catch (IOException e) {
            System.err.println("Error loading dwellings from file: " + e.getMessage());
        }
    }

    // Define saveHouses()
    private void saveHouses() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("dwellings.txt"));
            for (DwellingInfo dwelling : dwellingsDeque) {
                String dwellingType = dwelling instanceof House ? "House" : "Apartment";
                String line = dwellingType + "," + dwelling.getAddress() + "," + dwelling.getPriceOrRent() + "," + dwelling.getBedrooms() + "," + dwelling.getBathrooms();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving houses to file: " + e.getMessage());
        }
    }

    private void showDwellingTypeSelectionDialog(TableView<DwellingInfo> table) {
        Stage dialog = new Stage();
        dialog.setTitle("Add Dwelling");

        Button houseButton = new Button("House");
        Button apartmentButton = new Button("Apartment");

        houseButton.setOnAction(event -> {
            showHouseDialog(table, null);
            dialog.close();
        });

        apartmentButton.setOnAction(event -> {
            showApartmentDialog(table, null);
            dialog.close();
        });

        HBox hbox = new HBox(150, houseButton, apartmentButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));

        Scene scene = new Scene(hbox);
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    // Code for adding apartments
    private void showApartmentDialog(TableView<DwellingInfo> table, Apartment apartment) {
        Stage dialog = new Stage();
        dialog.setTitle(apartment == null ? "Add Apartment" : "Edit Apartment");

        Label addressLabel = new Label("Address:");
        TextField addressTextField = new TextField(apartment == null ? "" : apartment.getAddress());

        Label rentLabel = new Label("Rent:");
        TextField rentTextField = new TextField(apartment == null ? "" : Double.toString(apartment.getRent()));

        Label bedroomsLabel = new Label("Bedrooms:");
        TextField bedroomsTextField = new TextField(apartment == null ? "" : Integer.toString(apartment.getBedrooms()));

        Label bathroomsLabel = new Label("Bathrooms:");
        TextField bathroomsTextField = new TextField(apartment == null ? "" : Double.toString(apartment.getBathrooms()));

        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.add(addressLabel, 0, 0);
        gridPane.add(addressTextField, 1, 0);
        gridPane.add(rentLabel, 0, 1);
        gridPane.add(rentTextField, 1, 1);
        gridPane.add(bedroomsLabel, 0, 2);
        gridPane.add(bedroomsTextField, 1, 2);
        gridPane.add(bathroomsLabel, 0, 3);
        gridPane.add(bathroomsTextField, 1, 3);
        gridPane.add(okButton, 0, 4);
        gridPane.add(cancelButton, 1, 4);

        okButton.setOnAction(event -> {
            String address = addressTextField.getText();
            String rentText = rentTextField.getText();
            String bedroomsText = bedroomsTextField.getText();
            String bathroomsText = bathroomsTextField.getText();

            if (address.trim().isEmpty() || rentText.trim().isEmpty() || bedroomsText.trim().isEmpty() || bathroomsText.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all fields.");
                alert.showAndWait();
            } else {
                double rent = Double.parseDouble(rentText);
                int bedrooms = Integer.parseInt(bedroomsText);
                double bathrooms = Double.parseDouble(bathroomsText);

                if (apartment == null) {
                    dwellingsDeque.add(new Apartment(address, rent, bedrooms, bathrooms));
                } else {
                    apartment.setAddress(address);
                    apartment.setRent(rent);
                    apartment.setBedrooms(bedrooms);
                    apartment.setBathrooms(bathrooms);
                }

                saveHouses();
                quickSort(dwellingsDeque, 0, dwellingsDeque.size() - 1);
                table.refresh();
                dialog.close();
            }
        });

        cancelButton.setOnAction(event -> {
            dialog.close();
        });

        Scene dialogScene = new Scene(gridPane, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    //Code for adding houses
    private void showHouseDialog(TableView<DwellingInfo> table, House house) {
        Stage dialog = new Stage();
        dialog.setTitle(house == null ? "Add House" : "Edit House");

        Label addressLabel = new Label("Address:");
        TextField addressTextField = new TextField(house == null ? "" : house.getAddress());

        Label priceLabel = new Label("Price:");
        TextField priceTextField = new TextField(house == null ? "" : Double.toString(house.getPrice()));

        Label bedroomsLabel = new Label("Bedrooms:");
        TextField bedroomsTextField = new TextField(house == null ? "" : Integer.toString(house.getBedrooms()));

        Label bathroomsLabel = new Label("Bathrooms:");
        TextField bathroomsTextField = new TextField(house == null ? "" : Double.toString(house.getBathrooms()));

        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.add(addressLabel, 0, 0);
        gridPane.add(addressTextField, 1, 0);
        gridPane.add(priceLabel, 0, 1);
        gridPane.add(priceTextField, 1, 1);
        gridPane.add(bedroomsLabel, 0, 2);
        gridPane.add(bedroomsTextField, 1, 2);
        gridPane.add(bathroomsLabel, 0, 3);
        gridPane.add(bathroomsTextField, 1, 3);
        gridPane.add(okButton, 0, 4);
        gridPane.add(cancelButton, 1, 4);

        okButton.setOnAction(event -> {
            String address = addressTextField.getText();
            String priceText = priceTextField.getText();
            String bedroomsText = bedroomsTextField.getText();
            String bathroomsText = bathroomsTextField.getText();

            if (address.trim().isEmpty() || priceText.trim().isEmpty() || bedroomsText.trim().isEmpty() || bathroomsText.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all fields.");
                alert.showAndWait();
            } else {
                double price = Double.parseDouble(priceText);
                int bedrooms = Integer.parseInt(bedroomsText);
                double bathrooms = Double.parseDouble(bathroomsText);

                if (house == null) {
                    dwellingsDeque.add(new House(address, price, bedrooms, bathrooms));
                } else {
                    house.setAddress(address);
                    house.setPrice(price);
                    house.setBedrooms(bedrooms);
                    house.setBathrooms(bathrooms);
                }

                saveHouses();
                quickSort(dwellingsDeque, 0, dwellingsDeque.size() - 1);
                table.refresh();
                dialog.close();
            }
        });

        cancelButton.setOnAction(event -> {
            dialog.close();
        });

        Scene dialogScene = new Scene(gridPane, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    // Implementation of quickSort which has an average time complexity O(nlogn)
    private static void quickSort(Deque<DwellingInfo> dwellingsDeque, int low, int high) {
        if (low < high) {
            int pi = partition(dwellingsDeque, low, high);

            quickSort(dwellingsDeque, low, pi - 1);
            quickSort(dwellingsDeque, pi + 1, high);
        }
    }

    private static int partition(Deque<DwellingInfo> dwellings, int low, int high) {
        DwellingInfo[] dwellingsArray = dwellings.toArray(new DwellingInfo[dwellings.size()]);
        DwellingInfo pivot = dwellingsArray[high];
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            if (dwellingsArray[j].getPriceOrRent() < pivot.getPriceOrRent()) {
                i++;
                DwellingInfo temp = dwellingsArray[i];
                dwellingsArray[i] = dwellingsArray[j];
                dwellingsArray[j] = temp;
            }
        }

        DwellingInfo temp = dwellingsArray[i + 1];
        dwellingsArray[i + 1] = dwellingsArray[high];
        dwellingsArray[high] = temp;

        // Update the Deque with the modified order
        dwellings.clear();
        for (DwellingInfo dwelling : dwellingsArray) {
            dwellings.add(dwelling);
        }

        return i + 1;
    }

    public static void main(String[] args) {
        loadHouses();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadHouses();
        quickSort(dwellingsDeque, 0, dwellingsDeque.size() - 1);

        TableView<DwellingInfo> table = new TableView<>(FXCollections.observableList(new ArrayList<>(dwellingsDeque)));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DwellingInfo, String> addressColumn = new TableColumn<>("Address");
        TableColumn<DwellingInfo, Number> priceColumn = new TableColumn<>("Price");
        TableColumn<DwellingInfo, Number> rentColumn = new TableColumn<>("Rent");
        TableColumn<DwellingInfo, Number> bedroomsColumn = new TableColumn<>("Bedrooms");
        TableColumn<DwellingInfo, Number> bathroomsColumn = new TableColumn<>("Bathrooms");

        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        priceColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof House) {
                return new ReadOnlyDoubleWrapper(((House) cellData.getValue()).getPrice()).getReadOnlyProperty();
            }
            return new ReadOnlyDoubleWrapper(0).getReadOnlyProperty();
        });

        rentColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Apartment) {
                return new ReadOnlyDoubleWrapper(((Apartment) cellData.getValue()).getRent()).getReadOnlyProperty();
            }
            return new ReadOnlyDoubleWrapper(0).getReadOnlyProperty();
        });

        bedroomsColumn.setCellValueFactory(cellData -> cellData.getValue().bedroomsProperty());
        bathroomsColumn.setCellValueFactory(cellData -> cellData.getValue().bathroomsProperty());

        table.getColumns().addAll(addressColumn, priceColumn, rentColumn, bedroomsColumn, bathroomsColumn);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            showDwellingTypeSelectionDialog(table);
            table.setItems(FXCollections.observableList(new ArrayList<>(dwellingsDeque))); // Update the table items
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            DwellingInfo selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.isHouse()) {
                    showHouseDialog(table, (House) selectedItem);
                } else {
                    showApartmentDialog(table, (Apartment) selectedItem);
                }
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            DwellingInfo selectedDwelling = table.getSelectionModel().getSelectedItem();
            if (selectedDwelling != null) {
                dwellingsDeque.remove(selectedDwelling);
                saveHouses();
                quickSort(dwellingsDeque, 0, dwellingsDeque.size() - 1);
                table.setItems(FXCollections.observableList(new ArrayList<>(dwellingsDeque))); // Update the table items
            }
        });

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane root = new BorderPane(table);
        root.setPadding(new Insets(10));
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("House Comparison");
        primaryStage.setOnCloseRequest(event -> {
            saveHouses();
            System.exit(0);
        });
        primaryStage.show();
    }


}