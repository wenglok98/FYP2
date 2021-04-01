package com.example.fyp2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp2.Activity.ReadNotesActivity;
import com.example.fyp2.Activity.SettingActivity;
import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.Class.ReviewCommentClass;
import com.example.fyp2.Utils.SnackUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static io.realm.Realm.getApplicationContext;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {
    Context context;
    ArrayList<NotesClass> subjectListAdapterArrayList = new ArrayList<>();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showChangeLanguageDialog(notesClass.getFirebase_id(),position);
                return false;
            }
        });
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

    private void showChangeLanguageDialog(String firebaseid,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete the Notes?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletfromFirebase(firebaseid,position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void deletfromFirebase(String firebaseid,int position) {


        Task<QuerySnapshot> documentReference = fStore.collection("Notes")
                .whereEqualTo("firebase_id", firebaseid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        fStore.collection("Notes").document(firebaseid)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                subjectListAdapterArrayList.remove(position);
                                notifyDataSetChanged();
                                SnackUtil.show(context,"Deleted");


                            }
                        });

                    }
                });



    }
}
