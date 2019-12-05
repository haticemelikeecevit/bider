import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bid implements Serializable {
    @SerializedName("username")
    public String username;

    @SerializedName("amount")
    public int amount;

    @SerializedName("productName")
    public String productName;

    public Bid(String username, int amount, String productName) {
        this.username = username;
        this.amount = amount;
        this.productName = productName;
    }
}
