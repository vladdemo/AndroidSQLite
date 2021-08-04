package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Worker {
    public static final String TABLE_NAME = "workers";
    public static final String TABLE_NAME_2 = "department";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORKERS_ID = "workers_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_ID_DEPARTMENT = "id_department";
    public static final String COLUMN_DATEOFBIRTH = "date_of_birth";
    public static final String COLUMN_DATEOFACCEPT = "date_of_accept";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String COLUMN_DEPARTMENT_NAME = "department_name";



    private int id;
    private int workers_id;
    private String name;
    private String surname;
    private int id_department;
    private String dateOfBirth;
    private String dateOfAccept;
    private String timestamp;
    private String department_name;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WORKERS_ID + " INTEGER,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SURNAME + " TEXT,"
                    + COLUMN_ID_DEPARTMENT + " INTEGER,"
                    + COLUMN_DATEOFBIRTH + " TEXT,"
                    + COLUMN_DATEOFACCEPT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (id_department) REFERENCES department (id_department)"
                    + ");";
    public static final String CREATE_TABLE_2 =
            "CREATE TABLE " + TABLE_NAME_2 + "("
                    + COLUMN_ID_DEPARTMENT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DEPARTMENT_NAME + " TEXT"
                    + ");";


    public Worker() {
    }

    public Worker(int id,
                  int workers_id,
                  String name,
                  String surname,
                  int id_department,
                  String dateOfBirth,
                  String dateOfAccept,
                  String timestamp,
                  String department_name) {
        this.id = id;
        this.workers_id = workers_id;
        this.name = name;
        this.surname = surname;
        this.id_department = id_department;
        this.dateOfBirth = dateOfBirth;
        this.dateOfAccept = dateOfAccept;
        this.timestamp = timestamp;

        this.department_name = department_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkersId() {
        return workers_id;
    }

    public void setWorkersId(int workers_id) {
        this.workers_id = workers_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId_department() {
        return id_department;
    }

    public void setId_department(int id_department) {
        this.id_department = id_department;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfAccept() {
        return dateOfAccept;
    }

    public void setDateOfAccept(String dateOfAccept) {
        this.dateOfAccept = dateOfAccept;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
