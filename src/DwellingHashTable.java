import java.util.HashMap;

public class DwellingHashTable {
    private HashMap<String, DwellingInfo> hashTable;

    public DwellingHashTable() {
        hashTable = new HashMap<>();
    }

    public void insert(DwellingInfo dwelling) {
        hashTable.put(dwelling.getAddress(), dwelling);
    }

    public DwellingInfo search(String address) {
        return hashTable.get(address);
    }

    public void remove(DwellingInfo dwelling) {
        hashTable.remove(dwelling.getAddress());
    }

    public boolean contains(String address) {
        return hashTable.containsKey(address);
    }
}