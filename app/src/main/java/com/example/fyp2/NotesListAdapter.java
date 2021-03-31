package com.example.fyp2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp2.Activity.ReadNotesActivity;
import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.Class.ReviewCommentClass;

import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {
    Context context;
    ArrayList<NotesClass> subjectListAdapterArrayList = new ArrayList<>();

    public NotesListAdapter(Context ct, ArrayList<NotesClass> subjectListAdapters) {
        context = ct;
        subjectListAdapterArrayList = subjectListAdapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_notes, parent, false);
        return new NotesListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotesClass notesClass = subjectListAdapterArrayList.get(position);

        holder.notesTime.setText(notesClass.getTimeStamp());
        holder.notestitle.setText(notesClass.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadNotesActivity.class);
                intent.putExtra("title", notesClass.getTitle());
                intent.putExtra("notes", notesClass.getNotes());
                intent.putExtra("timesStamp", notesClass.getTimeStamp());
                intent.putExtra("UID", notesClass.getUID());
                intent.putExtra("firebaseid", notesClass.getFirebase_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectListAdapterArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView notestitle;
        private TextView notesTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notesTime = itemView.findViewById(R.id.note_adapter_time);
            notestitle = itemView.findViewById(R.id.notes_title);
        }
    }
}
