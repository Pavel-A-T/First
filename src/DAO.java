import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;


public class DAO {

    private static String url = "jdbc:mysql://localhost:3306/data";
    private static Properties prop = new Properties();
    private static String nameDB = "data";
    private ArrayList<Student> student;

    public static String getNameDB() {
        return nameDB;
    }
    public void setNameDB(String nameDB) {
        this.nameDB = nameDB;
    }
    private static void setProperties() {
        prop.put("user","root");
        prop.put("password","root");
        prop.put("autoReconnect","true");
        prop.put("characterEncoding","UTF-8");
        prop.put("useUnicode","true");
}
    private static void createDB() throws SQLException {

        Connection con = DriverManager.getConnection(url, prop);
        Statement statement = con.createStatement();
        statement.executeUpdate("CREATE SCHEMA " + getNameDB() + " DEFAULT CHARACTER SET utf8");
               // +" GRANT ALL ON "+getNameDB()+".* TO root IDENTIFIED BY root; ");
        statement.close();
        con.close();
    }
    private static void createTableStudent() throws SQLException{
        Connection con = DriverManager.getConnection(url, prop);
        Statement statement = con.createStatement();
        statement.executeUpdate("CREATE TABLE " +getNameDB()+".student (idstudent INT UNSIGNED NOT NULL DEFAULT 1, "
                + " fio  VARCHAR(128) NULL, idsubject INT UNSIGNED NULL, PRIMARY KEY (idstudent), "
                + "UNIQUE INDEX idstudent_UNIQUE (idstudent ASC), UNIQUE INDEX fio_UNIQUE (fio ASC))"
                + "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8");
        statement.close();
        con.close();
    }
    private static void createTableSubject() throws SQLException{
        Connection con = DriverManager.getConnection(url, prop);
        Statement statement = con.createStatement();
        statement.executeUpdate("CREATE TABLE " +getNameDB()+".subject (idsubject INT UNSIGNED NOT NULL," +
                        " name VARCHAR(45), PRIMARY KEY (idsubject), UNIQUE INDEX idsubject_UNIQUE (idsubject ASC),"
                        + "UNIQUE INDEX name_UNIQUE (name ASC)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;");
        statement.close();
        con.close();

    }
    public boolean existenceCheckDB() {
        setProperties();


        try {

            Connection con = DriverManager.getConnection(url, prop);
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs;
            Statement statement = con.createStatement();
            rs = statement.executeQuery("SHOW DATABASES;");
            while(rs.next()){
                if(rs.getString("Database").equals(getNameDB())){
                    //DB уже существует ищем таблицы
                    String[] tableTypes = { "TABLE" };

                    ResultSet allTables = dbmd.getTables(null, getNameDB(), null, tableTypes);
                    ArrayList<String> list = new ArrayList();

                    while(allTables.next()) {
                        list.add(allTables.getString("TABLE_NAME"));

                    }
                    if (list.size() > 0) {
                        if (list.indexOf("student") == -1) {
                            createTableStudent();
                        }
                        if (list.indexOf("subject") == -1) {
                            createTableSubject();
                        }
                           con.close();
                           return true;
                    } else {
                        //Создаём таблицы
                        createTableStudent();
                        createTableSubject();
                        con.close();
                        return true;
                    }
                }
                else {
                    //Создать БД
                    createDB();
                    createTableStudent();
                    createTableSubject();
                    con.close();
                    return true;





                }

            }


            con.close();
        }

        catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
        return false;
    }

    public static void main(String[] args)  throws SQLException {
         DAO dao = new DAO();
         dao.student = dao.getStudent();
         for (Student st : dao.student) {
             if (st.getFio().equals("Петров Ф К")) {
                 dao.updateStudent(st, "Петр");
             }
         }
       // dao.student = dao.getStudent();
        for (Student st : dao.student) {
            System.out.println(st.getId()+"  "+st.getFio()+"  " + st.getSubject());
        }
        }

    public boolean updateStudent(Student st, String newFIO) throws SQLException {
        if (student.size() > 0) {
            if (student.indexOf(st) == -1 ) {
                return false;
            } else {
                try {
                    Connection con = DriverManager.getConnection(url, prop);
                    Statement statement = con.createStatement();
                    statement.executeUpdate("UPDATE student SET fio ='" + newFIO + "' WHERE idstudent = "+Integer.toString(st.getId()));
                    return true;
                } catch(SQLException e) {
                    System.out.println("SQL Exception: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }
   /* public boolean updateStudent(Student st, String subject) throws SQLException {
        if (student.size() > 0) {
            if (student.indexOf(st) == -1 ) {
                return false;
            } else {
                try {
                    Connection con = DriverManager.getConnection(url, prop);
                    Statement statement = con.createStatement();
                    statement.executeUpdate("UPDATE student" +
                            "SET fio = " + newFIO +
                            "WHERE idstudent = " + Integer.toString(st.getId()));
                    return true;
                } catch(SQLException e) {
                    System.out.println("SQL Exception: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }*/
    public void updateSubject() {

    }

    public ArrayList<Student> getStudent() throws SQLException{
        setProperties();
        Connection cn = null;
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        try { // 2  блок
            cn = DriverManager.getConnection(url, prop);
            Statement st = null;
            Statement stSubject = null;
            try {  // 2 блок
                st = cn.createStatement();
                stSubject = cn.createStatement();
                ResultSet rs = null;
                ResultSet rsSubject = null;
                try {  //  3 блок
                    rs = st.executeQuery("SELECT *  FROM student");
                    ArrayList<Student> lst = new ArrayList();
                    while (rs.next()) {
                        String name = "";
                        int id = rs.getInt(1);
                        String fio = rs.getString(2);
                        int idSubject = rs.getInt(3);
                        rsSubject = stSubject.executeQuery("SELECT name FROM subject WHERE (idsubject = " + Integer.toString(idSubject)+ ")");
                        while (rsSubject.next()) {
                                name = rsSubject.getString(1);
                        }
                        lst.add(new Student(id, fio, name));

                    }
                    if (lst.size() > 0) {
                       return lst;

                    } else {
                        return null;
                    }
                } finally {  //  для 3-го блока try
/*
* закрыть ResuLtSet,  если он был открыт
* или ошибка произошла во время
* чтения из него данных
*/
                    if (rs != null) { // был ли создан ResuLtSet
                        rs.close();
                        rsSubject.close();
                    } else {
                        System.err.println(
                                "ошибка во время чтения  из БД");
                    }
                }
            } finally {
/*
* закрьть Statement,  если он был открыт или ошибка
* произошла во время создания Statement
*/
                if (st != null) { // для 2-го блока;
                    st.close();
                    stSubject.close();
                } else {
                    System.err.println("Statement не создан");
                    return null;
                }
            }
        } catch (SQLException e) { // для 1-го блока try
            System.err.println("DB  connection error:  " + e);
            return null;
/*
* вывод сообщения о всех SQLException
*/
        } finally {
/*
* закрыть Connection,  если он был открыт
*/
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException e) {
                    System.err.println("Connection close error:  " + e);
                    return null;
                }
            }

        }
    }
}


