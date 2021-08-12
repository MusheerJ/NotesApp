package com.ok.notesappproto.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ok.notesappproto.databinding.ActivityAddNoteBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import Models.Note;

public class AddNoteActivity extends AppCompatActivity {

    ActivityAddNoteBinding binding;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Note note;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding note please wait ...");
        Uid = getIntent().getStringExtra("Uid");

        binding.addNoteToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binding.noteTitle.getText().toString().isEmpty()) {
                        binding.noteTitle.setError("This cant be empty !!");
                        return;
                    }

                    if (binding.noteDescription.getText().toString().isEmpty()) {
                        binding.noteDescription.setError("This cant be empty");
                        return;
                    }
                    note = new Note();
                    progressDialog.show();
                    note.setNoteDate(getDate());
                    note.setNoteTitle(binding.noteTitle.getText().toString());
                    note.setNoteDescription(binding.noteDescription.getText().toString());


                    database.getReference().child("Notes").child(Uid).child(note.getNoteDate()).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNoteActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                            setEditTextToNull();
                        }
                    });
                } catch (Exception e) {
                    Log.d("NoteAddingExcp", "onClick: ");
                }


            }
        });

    }

    private void setEditTextToNull() {
        binding.noteTitle.setText(null);
        binding.noteDescription.setText(null);
        binding.noteDescription.setError(null);
        binding.noteTitle.setError(null);
        binding.noteTitle.requestFocus();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private String getDate() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        return date.toString().substring(0, 19);
    }
}