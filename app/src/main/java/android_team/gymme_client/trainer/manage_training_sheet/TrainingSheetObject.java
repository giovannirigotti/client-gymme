package android_team.gymme_client.trainer.manage_training_sheet;

public class TrainingSheetObject {

    private String training_sheet_id;
    private String customer_id;
    private String trainer_id;
    private String creation_date;
    private String title;
    private String description;
    private String number_of_days;
    private String strength;
    private String name;
    private String lastname;


    public TrainingSheetObject(String training_sheet_id, String customer_id, String trainer_id, String creation_date, String title, String description, String number_of_days, String strength, String name, String lastname) {
        this.training_sheet_id = training_sheet_id;
        this.customer_id = customer_id;
        this.trainer_id = trainer_id;
        this.creation_date = creation_date.split("T")[0];
        this.title = title;
        this.description = description;
        this.number_of_days = number_of_days;
        this.strength = strength;
        this.name = name;
        this.lastname = lastname;
    }


    public String getTraining_sheet_id() {
        return training_sheet_id;
    }

    public void setTraining_sheet_id(String training_sheet_id) {
        this.training_sheet_id = training_sheet_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber_of_days() {
        return number_of_days;
    }

    public void setNumber_of_days(String number_of_days) {
        this.number_of_days = number_of_days;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "TrainingSheetObject{" +
                "training_sheet_id='" + training_sheet_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", trainer_id='" + trainer_id + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_days='" + number_of_days + '\'' +
                ", strength='" + strength + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
