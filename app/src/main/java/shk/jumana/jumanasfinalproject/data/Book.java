package shk.jumana.jumanasfinalproject.data;

public class Book
{
    //Define variables
    private String image;
    private String key;
    private String owner;
    private String Name;
    private String Author;
    private int Date;
    private String Genre;
    private String About;
    private int Rate;
    private int Age;

    public Book()
    {

    }
    //getters and setters

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }


    @Override
    public String toString() {
        return "Book{" +
                "key='" + key + '\'' +
                ", owner='" + owner + '\'' +
                ", Name='" + Name + '\'' +
                ", Author='" + Author + '\'' +
                ", Date=" + Date +
                ", Genre='" + Genre + '\'' +
                ", About='" + About + '\'' +
                ", Rate="+ Rate+
                ", Age=" + Age +
                ", image=" + image +
                '}';
    }



}
