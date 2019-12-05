import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("type")
    RequestType type;

    @SerializedName("product")
    Product product;

    @SerializedName("bid")
    Bid bid;

    @SerializedName("user")
    User user;
}
enum RequestType {
    GET_PRODUCTS, GET_BIDS, CREATE_BID, LOGIN
}