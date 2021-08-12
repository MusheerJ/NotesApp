package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ok.notesappproto.R;
import com.ok.notesappproto.databinding.NoteDeleteDialogBinding;
import com.ok.notesappproto.databinding.SampleNoteBinding;
import com.ok.notesappproto.databinding.ViewNoteDetailsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import Models.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    ArrayList<Note> notes;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @NotNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotesAdapter.NotesViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.binding.sampleNoteDate.setText(note.getNoteDate().substring(0, 16));
        holder.binding.sampleNoteTitle.setText(note.getNoteTitle());
        holder.binding.sampleNoteDescription.setText(getDescription(note.getNoteDescription()));

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(note);
            }
        });

        holder.binding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteNoteOption(note);
                return false;
            }
        });
    }

    private void deleteNoteOption(Note note) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_delete_dialog, null);
        NoteDeleteDialogBinding binding = NoteDeleteDialogBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .create();
        dialog.show();

        binding.CancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uid = FirebaseAuth.getInstance().getUid();
                FirebaseDatabase.getInstance().getReference()
                        .child("Notes")
                        .child(Uid)
                        .child(note.getNoteDate())
                        .setValue(null);
                Toast.makeText(context, "Note deleted ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
    }

    private void showAlertDialog(Note note) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_note_details, null);
        ViewNoteDetailsBinding binding = ViewNoteDetailsBinding.bind(view);
        binding.noteDate.setText(note.getNoteDate().substring(0, 16));
        binding.noteTitleInDetails.setText(note.getNoteTitle());
        binding.noteDesciptionDetails.setText(note.getNoteDescription());
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .create();
        dialog.show();
    }

    private String getDescription(String noteDescription) {
        if (noteDescription.length() >= 50) {
            return noteDescription.substring(0, 50) + " ....";
        } else return noteDescription;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void filter(ArrayList<Note> filteredNotes) {
        notes = filteredNotes;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        SampleNoteBinding binding;

        public NotesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleNoteBinding.bind(itemView);
        }
    }
}
