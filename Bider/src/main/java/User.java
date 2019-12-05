import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
