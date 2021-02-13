package android_team.gymme_client.trainer.menage_trainig_sheet;

public class CompleteExerciseObject {
    private String training_sheet_id;
    private String seq;
    private String repetitions;
    private String exercise_id;
    private String name;

    public CompleteExerciseObject(String training_sheet_id, String seq, String repetitions, String exercise_id, String name) {
        this.training_sheet_id = training_sheet_id;
        this.seq = seq;
        this.repetitions = repetitions;
        this.exercise_id = exercise_id;
        this.name = name;
    }

    public String getTraining_sheet_id() {
        return training_sheet_id;
    }

    public void setTraining_sheet_id(String training_sheet_id) {
        this.training_sheet_id = training_sheet_id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
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
        return "CompleteExerciseObject{" +
                "training_sheet_id='" + training_sheet_id + '\'' +
                ", seq='" + seq + '\'' +
                ", repetitions='" + repetitions + '\'' +
                ", exercise_id='" + exercise_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
