package android_team.gymme_client.nutritionist;

public class NutritionistObject {
    public String user_id;
    public String name;
    public String lastname;
    public String email;
    public String qualification;
    public String fiscal_code;

    public NutritionistObject(String user_id, String name, String lastname, String email, String qualification, String fiscal_code) {
        this.user_id = user_id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.qualification = qualification;
        this.fiscal_code = fiscal_code;
    }

    @Override
    public String toString() {
        return "NutritionistObject{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", qualification='" + qualification + '\'' +
                ", fiscal_code='" + fiscal_code + '\'' +
                '}';
    }
}
