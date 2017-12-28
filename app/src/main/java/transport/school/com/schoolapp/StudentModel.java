package transport.school.com.schoolapp;

/**
 * Created by Naveen.Goyal on 11/29/2017.
 */

public class StudentModel {

    private String name;
    private int thumbnail;

    public StudentModel() {
    }

    public StudentModel(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
