import javafx.beans.property.*;

public abstract class DwellingInfo {
    private final StringProperty address;
    private final IntegerProperty bedrooms;
    private final DoubleProperty bathrooms;

    public DwellingInfo(String address, int bedrooms, double bathrooms) {
        this.address = new SimpleStringProperty(address);
        this.bedrooms = new SimpleIntegerProperty(bedrooms);
        this.bathrooms = new SimpleDoubleProperty(bathrooms);
    }

    public boolean isHouse() {
        return this instanceof House;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getBedrooms() {
        return bedrooms.get();
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms.set(bedrooms);
    }

    public double getBathrooms() {
        return bathrooms.get();
    }

    public void setBathrooms(double bathrooms) {
        this.bathrooms.set(bathrooms);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public IntegerProperty bedroomsProperty() {
        return bedrooms;
    }

    public DoubleProperty bathroomsProperty() {
        return bathrooms;
    }

    public abstract double getPriceOrRent();
}