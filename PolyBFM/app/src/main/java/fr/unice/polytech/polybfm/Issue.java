package fr.unice.polytech.polybfm;

public class Issue {
    private int key;
    private String title;
    private String reporter;
    private String emergency;
    private String category;
    private String place;
    private String date;
    private String pathToPhoto;
    private boolean viewed;
    private boolean deleted;

    Issue(int key, String title, String reporter, String emergency, String category, String place, String date, String pathToPhoto, boolean viewed, boolean deleted) {
        this.key = key;
        this.title = title;
        this.reporter = reporter;
        this.emergency = emergency;
        this.category = category;
        this.place = place;
        this.date = date;
        this.pathToPhoto = pathToPhoto;
        this.viewed = viewed;
        this.deleted = deleted;
    }


    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public String getTitle() {
        return title;
    }

    public int getKey() {
        return key;
    }

    public String getReporter() {
        return reporter;
    }

    public String getEmergency() {
        return emergency;
    }

    public String getCategory() {
        return category;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public boolean isViewed() {
        return viewed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void viewIssue(){
        this.viewed=true;
    }

    public void deleteIssue(){
        this.deleted=true;
    }
}
