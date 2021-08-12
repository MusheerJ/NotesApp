package Models;

public class Note {
    private String noteDate, noteTitle, noteDescription;

    public Note() {
    }


    public Note(String noteDate, String noteTitle, String noteDescription, String noteId) {
        this.noteDate = noteDate;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;

    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }


}
