package android_team.gymme_client.customer;

public class CustomerSmallObject {



    private String user_id;
    private String name;
    private String lasname;
    private String email;
    private String birthdate;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLasname() {
        return lasname;
    }

    public void setLasname(String lasname) {
        this.lasname = lasname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


    public CustomerSmallObject(String user_id, String name, String lasname, String email, String birthdate) {
        this.user_id = user_id;
        this.name = name;
        this.lasname = lasname;
        this.email = email;
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "CustomerSmallObject{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", lasname='" + lasname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }
}
