package android_team.gymme_client.gym;


public class GymObject {
    public String user_id;
    public String vat_number;
    public String gym_name;
    public String gym_address;
    public String zip_code;
    public Integer pool;
    public Integer box_ring;
    public Integer aerobics;
    public Integer spa;
    public Integer wifi;
    public Integer parking_area;
    public Integer personal_trainer_service;
    public Integer nutritionist_service;
    public Integer impedance_balance;
    public Integer courses;
    public Integer showers;



    public GymObject(String user_id, String vat_number, String gym_name, String gym_address, String zip_code, Integer pool, Integer box_ring, Integer aerobics, Integer spa, Integer wifi, Integer parking_area, Integer personal_trainer_service,
                     Integer nutritionist_service, Integer impedance_balance, Integer courses, Integer showers) {
        this.user_id = user_id;
        this.vat_number = vat_number;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.zip_code = zip_code;
        this.pool = pool;
        this.box_ring = box_ring;
        this.aerobics = aerobics;
        this.spa = spa;
        this.wifi = wifi;
        this.parking_area = parking_area;
        this.personal_trainer_service = personal_trainer_service;
        this.nutritionist_service = nutritionist_service;
        this.impedance_balance = impedance_balance;
        this.courses = courses;
        this.showers = showers;

    }

    @Override
    public String toString() {
        return "NutritionistObject{" +
                "user_id='" + user_id + '\'' +
                ", name='" + gym_name + '\'' +
                ", gym address='" + gym_address + '\'' +
                '}';
    }
}
