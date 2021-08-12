package com.ok.notesappproto.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ok.notesappproto.R;
import com.ok.notesappproto.databinding.ActivityViewNotesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import Adapters.NotesAdapter;
import Models.Note;

public class ViewNotesActivity extends AppCompatActivity {

    ActivityViewNotesBinding binding;
    FirebaseDatabase database;
    String Uid;
    ArrayList<Note> notes;
    NotesAdapter notesAdapter;
    static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        Uid = getIntent().getStringExtra("Uid");
        progressDialog.setMessage("Loading notes ...");


        notes = new ArrayList<>();
        notesAdapter = new NotesAdapter(this, notes);

        binding.notesRecylerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.notesRecylerView.setAdapter(notesAdapter);
        progressDialog.show();
        
        getDataFromDataBase();

    }

    private void getDataFromDataBase() {

        database.getReference().child("Notes").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressDialog.dismiss();
                    binding.dontHaveAnyNote.setVisibility(View.VISIBLE);
                    return;
                }
                binding.dontHaveAnyNote.setVisibility(View.GONE);
                notes.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Note note = snapshot1.getValue(Note.class);
                    notes.add(note);
                }
                Collections.reverse(notes);
                notesAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchNote);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search note");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filterNotes(String newText) {
        ArrayList<Note> filteredNotes = new ArrayList<>();
        if (newText.isEmpty()) {
            filteredNotes.addAll(notes);
            notesAdapter.filter(filteredNotes);
            return;
        } else {
            for (Note note : notes) {
                if (note.getNoteTitle().toLowerCase().contains(newText) ||
                        note.getNoteDescription().toLowerCase().contains(newText) ||
                        note.getNoteDate().contains(newText)) {
                    filteredNotes.add(note);
                }
            }
            notesAdapter.filter(filteredNotes);

        }
    }
}