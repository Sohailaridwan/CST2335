package com.example.sohaila.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String sendBackMsgToOne = "You selected item 1";
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //fab = (FloatingActionButton) findViewById(R.id.fab);
        fab = (FloatingActionButton) findViewById(R.id.floatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {

            case R.id.action_one:
                Log.i("Toolbar", "You selected item 1");
                Snackbar.make(findViewById(R.id.my_toolbar),sendBackMsgToOne, Snackbar.LENGTH_LONG).show();
                break;
            case R.id.action_two:
                Log.i("Toolbar", "Option 2 selected");
                // AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplication());
                //  AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                //  AlertDialog.Builder builder = new AlertDialog.Builder( this.getApplication() );
                builder.setTitle(R.string.pick_color);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_three:
                Log.i("Toolbar", "Option 3 is selected");
                LayoutInflater inflater = this.getLayoutInflater();
                //inflating the layout that contains custom message
                View view = inflater.inflate(R.layout.layout_to_signin,null);
                final EditText customText = (EditText) view.findViewById(R.id.customMsg);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TestToolbar.this);
                builder1.setView(view).setMessage(R.string.dialog_message);
                builder1.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendBackMsgToOne = customText.getText().toString();
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });

                // Create the AlertDialog object and return it
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case R.id.action_settings:
                Toast.makeText(this.getApplicationContext(), "Version 1.0 by Sohaila Ridwan",
                        Toast.LENGTH_LONG).show();
            default:
                break;
        }
        return true;
    }

}