package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.Worker;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "workers_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create two tables
        db.execSQL(Worker.CREATE_TABLE_2);
        db.execSQL(Worker.CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Worker.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Worker.TABLE_NAME_2);

        // Create tables again
        onCreate(db);
    }

    public long insertWorker(int workersId, String name, String surname, String department,
                             String dateOfBirth,
                             String dateOfAccept) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // get list of all department's names
        List<String> departments = new ArrayList<>();
        Cursor cursor_check = db.rawQuery("SELECT " + Worker.COLUMN_DEPARTMENT_NAME +
                " FROM " + Worker.TABLE_NAME_2, null);
        if (cursor_check.moveToFirst()) {
            do {
                departments.add(cursor_check.getString(cursor_check.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME)));
            } while (cursor_check.moveToNext());
        }

        // insert data into department table if it didn't exist already
        if (!departments.contains(department)) {
            ContentValues values2 = new ContentValues();
            values2.put(Worker.COLUMN_DEPARTMENT_NAME, department);
            long id2 = db.insert(Worker.TABLE_NAME_2, null, values2);
        }

        // get idDepartment
        Cursor cursor = db.query(Worker.TABLE_NAME_2,
                new String[]{Worker.COLUMN_ID_DEPARTMENT, Worker.COLUMN_DEPARTMENT_NAME},
                Worker.COLUMN_DEPARTMENT_NAME + "=?",
                new String[]{String.valueOf(department)}, null, null, null, null);
        cursor.moveToFirst();
        int id_department= cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT));



        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add them
        values.put(Worker.COLUMN_WORKERS_ID, workersId);
        values.put(Worker.COLUMN_NAME, name);
        values.put(Worker.COLUMN_SURNAME, surname);
        values.put(Worker.COLUMN_ID_DEPARTMENT, id_department);
        values.put(Worker.COLUMN_DATEOFBIRTH, dateOfBirth);
        values.put(Worker.COLUMN_DATEOFACCEPT, dateOfAccept);


        // insert row
        long id = db.insert(Worker.TABLE_NAME, null, values);



        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Worker getWorker(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Worker.TABLE_NAME,
                new String[]{Worker.COLUMN_ID, Worker.COLUMN_WORKERS_ID, Worker.COLUMN_NAME,
                        Worker.COLUMN_SURNAME,
                        Worker.COLUMN_ID_DEPARTMENT,
                        Worker.COLUMN_DATEOFBIRTH,
                        Worker.COLUMN_DATEOFACCEPT,
                        Worker.COLUMN_TIMESTAMP},
                Worker.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Cursor cursor2 = db.query(Worker.TABLE_NAME_2,
                new String[]{Worker.COLUMN_ID_DEPARTMENT, Worker.COLUMN_DEPARTMENT_NAME},
                Worker.COLUMN_ID_DEPARTMENT + "=?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT)))},
                null, null, null, null);
        cursor2.moveToFirst();
        //int IdDepartment= cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT));

        // prepare worker object
        Worker worker = new Worker(
                cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_WORKERS_ID)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_SURNAME)),
                cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)),
                cursor2.getString(cursor2.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME)));

        // close the db connection
        cursor.close();
        cursor2.close();

        return worker;
    }

    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Worker.TABLE_NAME  + " INNER JOIN " + Worker.TABLE_NAME_2 +
              " USING (" + Worker.COLUMN_ID_DEPARTMENT + ") " + " ORDER BY " + Worker.COLUMN_WORKERS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Worker worker = new Worker();
                worker.setId(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID)));
                worker.setWorkersId(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_WORKERS_ID)));
                worker.setName(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_NAME)));
                worker.setSurname(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_SURNAME)));
                worker.setId_department(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT)));
                worker.setDateOfBirth(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)));
                worker.setDateOfAccept(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)));
                worker.setTimestamp(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)));
                worker.setDepartment_name(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME)));
                workers.add(worker);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return workers;
    }

    public int getWorkersCount() {
        String countQuery = "SELECT  * FROM " + Worker.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateWorker(Worker worker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        values.put(Worker.COLUMN_WORKERS_ID, worker.getWorkersId());
        values.put(Worker.COLUMN_NAME, worker.getName());
        values.put(Worker.COLUMN_SURNAME, worker.getSurname());
        //values.put(Worker.COLUMN_ID_DEPARTMENT, worker.getId_department());
        values.put(Worker.COLUMN_DATEOFBIRTH, worker.getDateOfBirth());
        values.put(Worker.COLUMN_DATEOFACCEPT, worker.getDateOfAccept());
        values2.put(Worker.COLUMN_DEPARTMENT_NAME, worker.getDepartment_name());

        // get list of all department's names and updating department table, if ???
        List<String> departments = new ArrayList<>();
        Cursor cursor_check = db.rawQuery("SELECT " + Worker.COLUMN_DEPARTMENT_NAME +
                " FROM " + Worker.TABLE_NAME_2, null);
        if (cursor_check.moveToFirst()) {
            do {
                departments.add(cursor_check.getString(cursor_check.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME)));
            } while (cursor_check.moveToNext());
        }

        Cursor cursor_old_dep_name = db.rawQuery("SELECT " + Worker.COLUMN_DEPARTMENT_NAME +
                " FROM " + Worker.TABLE_NAME_2 + " WHERE " + Worker.COLUMN_ID_DEPARTMENT + "=?",
                new String[] {String.valueOf(worker.getId_department())});
        cursor_old_dep_name.moveToFirst();
        String oldDepartmentName = cursor_old_dep_name.getString(cursor_old_dep_name.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME));
        String newDepartmentName = worker.getDepartment_name();

        //
        if(!newDepartmentName.equals(oldDepartmentName)) {
            if (!departments.contains(newDepartmentName)) {
                long id2 = db.insert(Worker.TABLE_NAME_2, null, values2);
                Cursor cursor_new_id_dep = db.rawQuery(
                        "SELECT " + Worker.COLUMN_ID_DEPARTMENT +
                                " FROM " + Worker.TABLE_NAME_2
                                + " WHERE " + Worker.COLUMN_DEPARTMENT_NAME + "=?",
                        new String[] {newDepartmentName});
                cursor_new_id_dep.moveToFirst();
                int new_id_dep = cursor_new_id_dep.getInt(cursor_new_id_dep.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT));
                values.put(Worker.COLUMN_ID_DEPARTMENT, new_id_dep);

            } if (departments.contains(newDepartmentName)) {
                Cursor cursor_new_id_dep = db.rawQuery(
                        "SELECT " + Worker.COLUMN_ID_DEPARTMENT +
                                " FROM " + Worker.TABLE_NAME_2
                                + " WHERE " + Worker.COLUMN_DEPARTMENT_NAME + "=?",
                        new String[] {newDepartmentName});
                cursor_new_id_dep.moveToFirst();
                int new_id_dep = cursor_new_id_dep.getInt(cursor_new_id_dep.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT));
                values.put(Worker.COLUMN_ID_DEPARTMENT, new_id_dep);
            }
        } else {
            values.put(Worker.COLUMN_ID_DEPARTMENT, worker.getId_department());
        }


        // updating row
        return db.update(Worker.TABLE_NAME, values, Worker.COLUMN_ID + " = ?",
                new String[]{String.valueOf(worker.getId())});
    }

    public void deleteWorker(Worker worker) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Worker.TABLE_NAME, Worker.COLUMN_ID + " = ?",
                new String[]{String.valueOf(worker.getId())});
        db.close();
    }

    public List<Worker> sortWorkersById(int flag) {
        List<Worker> workers = new ArrayList<>();
        String direction;
        if (flag>0) {
            direction=" DESC";} else {
            direction=" ASC";}

        // Select All Query

        String selectQuery = "SELECT * FROM " + Worker.TABLE_NAME  + " INNER JOIN " + Worker.TABLE_NAME_2 +
                " USING (" + Worker.COLUMN_ID_DEPARTMENT + ") " +" ORDER BY " + Worker.COLUMN_WORKERS_ID + direction;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Worker worker = new Worker();
                worker.setId(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID)));
                worker.setWorkersId(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_WORKERS_ID)));
                worker.setName(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_NAME)));
                worker.setSurname(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_SURNAME)));
                worker.setId_department(cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID_DEPARTMENT)));
                worker.setDateOfBirth(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)));
                worker.setDateOfAccept(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)));
                worker.setTimestamp(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)));
                worker.setDepartment_name(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DEPARTMENT_NAME)));
                workers.add(worker);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return workers;
    }
}
