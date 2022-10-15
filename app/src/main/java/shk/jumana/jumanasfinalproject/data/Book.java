package shk.jumana.jumanasfinalproject.data;

public class Book
{


    private String key;
    private String title;
    private String subject;
    private String owners;


    public Book()
    {

    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }




    @Override
    public String toString() {
        return "Book{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", owners='" + owners + '\'' +
                '}';
    }







}
