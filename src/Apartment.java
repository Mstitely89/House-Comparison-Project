import javafx.beans.property.*;

public class Apartment extends DwellingInfo {
    private final DoubleProperty rent;

    public Apartment(String address, double rent, int bedrooms, double bathrooms) {
        super(address, bedrooms, bathrooms);
        this.rent = new SimpleDoubleProperty(rent);
    }

    public double getRent() {
        return rent.get();
    }

    public void setRent(double rent) {
        this.rent.set(rent);
    }

    public DoubleProperty rentProperty() {
        return rent;
    }

    @Override
    public double getPriceOrRent() {
        return getRent();
    }
}