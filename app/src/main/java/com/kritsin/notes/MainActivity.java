package com.kritsin.notes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kritsin.notes.api.SyncTask;
import com.kritsin.notes.db.DbCommonHelper;
import com.kritsin.notes.db.NoteDbAdapter;
import com.kritsin.notes.model.Note;
import com.kritsin.notes.model.ServerResponseStatus;
import com.kritsin.notes.model.User;
import com.kritsin.notes.util.PreferenceUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SyncTask.OnSyncListener {

    private ProgressDialog mProgressDialog;
    private AlertDialog mInputNoteDialog;
    private ListView mListView;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();
        initView();
        initDlg();

        loadData();
    }

    private void initDlg(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(R.string.please_wait));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите заметку");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if(text.trim().length()>0) {
                    Note note = new Note();
                    note.setNote(text);
                    note.setUserId(mUser.getServerId());
                    DbCommonHelper db = new DbCommonHelper(MainActivity.this);
                    NoteDbAdapter dbAdapter = new NoteDbAdapter();
                    dbAdapter.addNote(db.open(), note);

                    input.setText("");

                    loadData();
                }
                else {
                    mInputNoteDialog.show();
                    Toast.makeText(MainActivity.this,"Введите заметку",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mInputNoteDialog = builder.create();
    }

    private void initMenu(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputNoteDialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mUser = PreferenceUtils.getUser(MainActivity.this);
        ((TextView) header.findViewById(R.id.name)).setText(mUser.getName());
        ((TextView) header.findViewById(R.id.login)).setText(mUser.getLogin());
    }

    private void initView(){
        mListView = (ListView)findViewById(R.id.listView);
    }

    private void loadData(){
        DbCommonHelper db = new DbCommonHelper(this);
        NoteDbAdapter dbAdapter = new NoteDbAdapter();
        List<Note> data = dbAdapter.getNotes(db.open(),mUser.getServerId());
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mProgressDialog.show();
            new SyncTask(this, mUser.getServerId(), this).execute(new Void[]{});
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exit) {
            PreferenceUtils.removeUser(this);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onUserLoginResult(ServerResponseStatus responseStatus) {
        mProgressDialog.dismiss();
        if(responseStatus==ServerResponseStatus.OK){
            loadData();
        }
        else{
            Toast.makeText(this, responseStatus.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
