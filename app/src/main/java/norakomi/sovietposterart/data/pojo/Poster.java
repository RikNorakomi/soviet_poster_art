package norakomi.sovietposterart.data.pojo;

import norakomi.sovietposterart.Adapters.GridItem;

/**
 * Created by MEDION on 8-10-2015.
 */
public class Poster extends GridItem {

    private String title;
    private String author;
    private String filepath;
    private String filename;
    private String category;
    private String year;

    public Poster(String title, String author, String filepath, String filename, String category, String year) {

        super(100, title, filepath);
        this.title = title;
        this.author = author;
        this.filepath = filepath;
        this.filename = filename;
        this.category = category;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", filepath='" + filepath + '\n' +
                ", filename='" + filename + '\n' +
                ", category='" + category + '\n' +
                ", year='" + year + '\n' +
                '}' + '\n';
    }
}
