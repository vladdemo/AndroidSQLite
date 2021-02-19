package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Worker {
    public static final String TABLE_NAME = "workers";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORKERS_ID = "workers_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_DEPARTMENT = "department";
    public static final String COLUMN_DATEOFBIRTH = "date_of_birth";
    public static final String COLUMN_DATEOFACCEPT = "date_of_accept";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private int workers_id;
    private String name;
    private String surname;
    private String department;
    private String dateOfBirth;
    private String dateOfAccept;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WORKERS_ID + " INTEGER,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SURNAME + " TEXT,"
                    + COLUMN_DEPARTMENT + " TEXT,"
                    + COLUMN_DATEOFBIRTH + " TEXT,"
                    + COLUMN_DATEOFACCEPT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Worker() {
    }

    public Worker(int id,
                  int workers_id,
                  String name,
                  String surname ,
                  String department ,
                  String dateOfBirth ,
                  String dateOfAccept ,
                  String timestamp) {
        this.id = id;
        this.workers_id = workers_id;
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.dateOfBirth = dateOfBirth;
        this.dateOfAccept = dateOfAccept;
        this.timestamp = timestamp;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
}
