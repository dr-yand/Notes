package com.kritsin.notes.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kritsin.notes.model.Note;

import java.util.ArrayList;
import java.util.List;


public class NoteDbAdapter {

	public void addNote(SQLiteDatabase db, Note note){
		try{
	    	db.beginTransaction();
    		db.execSQL("insert into " + DbCommonHelper.NOTES_TABLE_NAME + "(server_id, user_id, note) " +
							" values(?,?,?)",
					new String[]{note.getServerId()+"", note.getUserId()+"", note.getNote()});
	    	db.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw new SQLException();
    	} finally {
    	  db.endTransaction();
    	}    
	}

    public void addNotes(SQLiteDatabase db, List<Note> notes, int userId){
        try{
            db.beginTransaction();
            for(Note note:notes) {
                db.execSQL("insert into " + DbCommonHelper.NOTES_TABLE_NAME + "(server_id, user_id, note) " +
                                " values(?,?,?)",
                        new String[]{note.getServerId() + "", userId + "", note.getNote()});
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            db.endTransaction();
        }
    }

    public List<Note> getNotes(SQLiteDatabase db, int userId){
        List<Note> result = new ArrayList<Note>();

        String sql="select id, note, server_id from "
                +DbCommonHelper.NOTES_TABLE_NAME+" where user_id=?  order by id desc";

        Cursor c = db.rawQuery(sql,new String[]{userId+""});
        if (c != null) {
            while (c.moveToNext()) {
                Note note = getNoteFromCursor(c);
                result.add(note);
            }
        }
		c.close();
        return result;
    }

    public List<Note> getNotesForSending(SQLiteDatabase db, int userId){
        List<Note> result = new ArrayList<Note>();

        String sql="select id, note, server_id from "
                +DbCommonHelper.NOTES_TABLE_NAME+" where user_id=? and server_id=0 order by id desc";

        Cursor c = db.rawQuery(sql,new String[]{userId+""});
        if (c != null) {
            while (c.moveToNext()) {
                Note note = getNoteFromCursor(c);
                result.add(note);
            }
        }
        c.close();
        return result;
    }

    public void deleteNoteById(SQLiteDatabase db, int id){
        try{
            db.beginTransaction();
            db.execSQL("delete from " + DbCommonHelper.NOTES_TABLE_NAME + " where id=?",
                    new String[]{id+""});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteNotes(SQLiteDatabase db, int userId){
        try{
            db.beginTransaction();
            db.execSQL("delete from " + DbCommonHelper.NOTES_TABLE_NAME + " where user_id=? and server_id!=0",
                    new String[]{userId+""});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            db.endTransaction();
        }
    }

    private Note getNoteFromCursor(Cursor c){
		final int ID = c.getColumnIndex("id");
    	final int NOTE = c.getColumnIndex("note");
        final int SERVER_ID = c.getColumnIndex("server_id");
		Note note = new Note();
		note.setId(c.getInt(ID));
		note.setNote(c.getString(NOTE));
        note.setServerId(c.getInt(SERVER_ID));
		return note;
	}

}
