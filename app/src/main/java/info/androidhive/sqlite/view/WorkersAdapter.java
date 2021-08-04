package info.androidhive.sqlite.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Worker;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.MyViewHolder> {

    private Context context;
    private List<Worker> workersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView dot;
        //public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.editText_Name);
            dot = view.findViewById(R.id.dot);
            //timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public WorkersAdapter(Context context, List<Worker> workersList) {
        this.context = context;
        this.workersList = workersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.worker_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Worker worker = workersList.get(position);

        holder.name.setText(worker.getWorkersId()+ "  "+
                worker.getName()+"  "+
                worker.getSurname()+"  "+
                worker.getDepartment_name()+"  "+
                worker.getDateOfBirth()+"  "+
                worker.getDateOfAccept());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
       //holder.timestamp.setText(formatDate(worker.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return workersList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
