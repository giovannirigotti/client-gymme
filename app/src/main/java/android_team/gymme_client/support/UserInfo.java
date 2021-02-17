package android_team.gymme_client.support;

public class UserInfo {

    public static int user_id;
    public static int user_type;

    public static int getUser_id() {
        return user_id;
    }

    public static int getUser_type() {
        return user_type;
    }

    public static void setUser_id(int user_id) {
        UserInfo.user_id = user_id;
    }

    public static void setUser_type(int user_type) {
        UserInfo.user_type = user_type;
    }
}
