import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public String price;

    @SerializedName("imageUrl")
    public String imageUrl;

    public Product(String name, String description, String price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
