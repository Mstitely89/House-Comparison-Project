import javafx.beans.property.*;

public class House extends DwellingInfo {
    private final DoubleProperty price;

    public House(String address, double price, int bedrooms, double bathrooms) {
        super(address, bedrooms, bathrooms);
        this.price = new SimpleDoubleProperty(price);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    @Override
    public double getPriceOrRent() {
        return getPrice();
    }
}