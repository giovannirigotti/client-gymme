package android_team.gymme_client.gym.menage_course;

public class CourseObject {

    private String course_id;
    private String trainer_name;
    private String trainer_lastname;
    private String description;
    private String title;
    private String category;
    private String start_date;
    private String end_date;
    private String free_spaces;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getTrainer_lastname() {
        return trainer_lastname;
    }

    public void setTrainer_lastname(String trainer_lastname) {
        this.trainer_lastname = trainer_lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getFree_spaces() {
        return free_spaces;
    }

    public void setFree_spaces(String free_spaces) {
        this.free_spaces = free_spaces;
    }


    public CourseObject(String course_id,String trainer_name, String trainer_lastname, String description, String title, String category, String start_date, String end_date, String free_spaces) {
        this.course_id = course_id;
        this.trainer_name = trainer_name;
        this.trainer_lastname = trainer_lastname;
        this.description = description;
        this.title = title;
        this.category = category;
        this.start_date = start_date;
        this.end_date = end_date;
        this.free_spaces = free_spaces;
    }

    @Override
    public String toString() {
        return "CourseObject{" +
                "Course_id='" + course_id + '\'' +
                ", trainer_name='" + trainer_name + '\'' +
                ", trainer_lastname='" + trainer_lastname + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", free_spaces='" + free_spaces + '\'' +
                '}';
    }
}
