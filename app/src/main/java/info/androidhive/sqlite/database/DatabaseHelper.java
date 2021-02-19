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
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "workers_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Worker.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Worker.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertWorker(int workersId, String name, String surname, String department,
                             String dateOfBirth,
                             String dateOfAccept) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Worker.COLUMN_WORKERS_ID, workersId);
        values.put(Worker.COLUMN_NAME, name);
        values.put(Worker.COLUMN_SURNAME, surname);
        values.put(Worker.COLUMN_DEPARTMENT, department);
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
                        Worker.COLUMN_DEPARTMENT,
                        Worker.COLUMN_DATEOFBIRTH,
                        Worker.COLUMN_DATEOFACCEPT,
                        Worker.COLUMN_TIMESTAMP},
                Worker.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Worker worker = new Worker(
                cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(Worker.COLUMN_WORKERS_ID)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_SURNAME)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DEPARTMENT)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)),
                cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return worker;
    }

    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME + " ORDER BY " +
                Worker.COLUMN_TIMESTAMP + " DESC";

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
                worker.setDepartment(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DEPARTMENT)));
                worker.setDateOfBirth(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)));
                worker.setDateOfAccept(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)));
                worker.setTimestamp(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)));

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
        values.put(Worker.COLUMN_WORKERS_ID, worker.getWorkersId());
        values.put(Worker.COLUMN_NAME, worker.getName());
        values.put(Worker.COLUMN_SURNAME, worker.getSurname());
        values.put(Worker.COLUMN_DEPARTMENT, worker.getDepartment());
        values.put(Worker.COLUMN_DATEOFBIRTH, worker.getDateOfBirth());
        values.put(Worker.COLUMN_DATEOFACCEPT, worker.getDateOfAccept());


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
            direction="ASC";} else {
            direction="DESC";}

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME + " ORDER BY " +
                Worker.COLUMN_WORKERS_ID +" "+direction;

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
                worker.setDepartment(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DEPARTMENT)));
                worker.setDateOfBirth(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFBIRTH)));
                worker.setDateOfAccept(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_DATEOFACCEPT)));
                worker.setTimestamp(cursor.getString(cursor.getColumnIndex(Worker.COLUMN_TIMESTAMP)));

                workers.add(worker);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return workers;
    }
}
