import javafx.beans.property.*;

public class Dwelling extends DwellingInfo {
    private final boolean isHouse;
    private final DoubleProperty priceOrRent;

    public Dwelling(boolean isHouse, String address, double priceOrRent, int bedrooms, double bathrooms) {
        super(address, bedrooms, bathrooms);
        this.isHouse = isHouse;
        this.priceOrRent = new SimpleDoubleProperty(priceOrRent);
    }

    public boolean isHouse() {
        return isHouse;
    }

    public boolean isApartment() {
        return !isHouse;
    }

    public double getPriceOrRent() {
        return priceOrRent.get();
    }

    public void setPriceOrRent(double priceOrRent) {
        this.priceOrRent.set(priceOrRent);
    }

    public DoubleProperty priceOrRentProperty() {
        return priceOrRent;
    }

   /* @Override
    public double getPriceOrRent() {
        return getPriceOrRent();
    }*/
}