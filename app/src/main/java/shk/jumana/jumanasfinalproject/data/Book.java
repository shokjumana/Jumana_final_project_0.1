package shk.jumana.jumanasfinalproject.data;

public class Book
{
    private String key;
    private String owner;
    private String Title;
    private String name;
    private String Author;
    private int year;
    private int age;
    private String Genre;
    private int pages;
    private int rate;
    private String About;

    public Book()
    {

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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }


    @Override
    public String toString() {
        return "Book{" +
                "key='" + key + '\'' +
                ", owner='" + owner + '\'' +
                "Title ="+ Title+'\'' +
                ", name='" + name + '\'' +
                ", Author='" + Author + '\'' +
                ", year=" + year +
                ", age=" + age +
                ", Genre='" + Genre + '\'' +
                ", pages=" + pages +
                ", Rate = " +rate+
                ", About = "+ About+
                '}';
    }

}
