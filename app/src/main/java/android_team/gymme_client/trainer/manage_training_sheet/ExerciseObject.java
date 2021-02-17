package android_team.gymme_client.trainer.manage_training_sheet;

public class ExerciseObject {

    private String exercise_id;
    private String name;


    public ExerciseObject(String exercise_id, String name) {
        this.exercise_id = exercise_id;
        this.name = name;

    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "ExerciseObject{" +
                "exercise_id='" + exercise_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
