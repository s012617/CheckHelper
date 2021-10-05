package tw.com.gbtech.checkhelper.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"barcode","name","checkDate","store"})
public class Check {
    @NonNull
    public String barcode;
    @NonNull
    public String name;
    @NonNull
    public String checkDate;
    @NonNull
    public String store;

    public int quantity;
    public String update;

    public Check(@NonNull String barcode, @NonNull String name, @NonNull String checkDate, @NonNull String store, int quantity, String update) {
        this.barcode = barcode;
        this.name = name;
        this.checkDate = checkDate;
        this.store = store;
        this.quantity = quantity;
        this.update = update;
    }

    @Override
    public String toString() {
        return "Check{" +
                "barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", checkDate='" + checkDate + '\'' +
                ", store='" + store + '\'' +
                ", quantity=" + quantity +
                ", update='" + update + '\'' +
                '}';
    }
}
