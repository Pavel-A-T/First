
public class Student {
    private int id;
    private String fio;
    private String subject;

    Student(int id, String fio, String subject) {
        this.id = id;
        this.fio = fio;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getSubject() {
        return subject;
    }

}
