package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Worker;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private WorkersAdapter mAdapter;
    private List<Worker> workersList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noWorkersView;
    private Button sortById;
    private int direction = 1;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noWorkersView = findViewById(R.id.empty_workers_view);
        sortById = findViewById(R.id.sortById);

        db = new DatabaseHelper(this);

        workersList.addAll(db.getAllWorkers());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new WorkersAdapter(this, workersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyWorkers();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) { showActionsDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        sortById.findViewById(R.id.sortById).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            direction *= -1;
                workersList.clear();
                workersList.addAll(db.sortWorkersById(direction));
                mAdapter.notifyDataSetChanged();
            }
        });

    }



    /**
     * Inserting new worker in db
     * and refreshing the list
     */
    private void createWorker(int workersId, String name, String surname, String department, String dateOfBirth, String dateOfAccept) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertWorker(workersId, name, surname, department, dateOfBirth, dateOfAccept);

        // get the newly inserted note from db
        Worker n = db.getWorker(id);

        if (n != null) {
            // adding new note to array list at 0 position
            workersList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyWorkers();
        }
    }

    /**
     * Updating worker in db and updating
     * item in the list by its position
     */
    private void updateWorker(int workersId, String name, String surname, String department, String dateOfBirth, String dateOfAccept, int position) {
        Worker n = workersList.get(position);
        // updating note text
        n.setWorkersId(workersId);
        n.setName(name);
        n.setSurname(surname);
        n.setDepartment_name(department);
        n.setDateOfBirth(dateOfBirth);
        n.setDateOfAccept(dateOfAccept);

        // updating note in db
        db.updateWorker(n);

        // refreshing the list
        workersList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyWorkers();
    }

    /**
     * Deleting worker from SQLite and removing the
     * item from the list by its position
     */
    private void deleteWorker(int position) {
        // deleting the note from db
        db.deleteWorker(workersList.get(position));

        // removing the note from the list
        workersList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyWorkers();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, workersList.get(position), position);
                } else {
                    deleteWorker(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Worker worker, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.worker_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.editText_Name);
        final EditText inputWorkersID = view.findViewById(R.id.textEdit_workersId);
        final EditText inputSurname = view.findViewById(R.id.editText_Surname);
        final EditText inputDepartment = view.findViewById(R.id.editText_department);
        final EditText inputDateOfBirth = view.findViewById(R.id.editText_dateOfBirth);
        final EditText inputDateOfAccept = view.findViewById(R.id.editText_dateOfAccept);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_worker_title) : getString(R.string.lbl_edit_worker_title));

        if (shouldUpdate && worker != null) {
            inputName.setText(worker.getName());
            inputWorkersID.setText(String.valueOf(worker.getWorkersId()));
            inputSurname.setText(worker.getSurname());
            inputDepartment.setText(worker.getDepartment_name());
            inputDateOfBirth.setText(worker.getDateOfBirth());
            inputDateOfAccept.setText(worker.getDateOfAccept());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter all the data!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && worker != null) {
                    // update note by it's id
                    updateWorker(Integer.parseInt(inputWorkersID.getText().toString()),
                            inputName.getText().toString(),
                            inputSurname.getText().toString(),
                            inputDepartment.getText().toString(),
                            inputDateOfBirth.getText().toString(),
                            inputDateOfAccept.getText().toString(),
                            position);

                } else {
                    // create new note
                    createWorker(Integer.parseInt(inputWorkersID.getText().toString()),
                            inputName.getText().toString(),
                            inputSurname.getText().toString(),
                            inputDepartment.getText().toString(),
                            inputDateOfBirth.getText().toString(),
                            inputDateOfAccept.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty worker view
     */
    private void toggleEmptyWorkers() {
        // you can check notesList.size() > 0

        if (db.getWorkersCount() > 0) {
            noWorkersView.setVisibility(View.GONE);
        } else {
            noWorkersView.setVisibility(View.VISIBLE);
        }
    }
}
