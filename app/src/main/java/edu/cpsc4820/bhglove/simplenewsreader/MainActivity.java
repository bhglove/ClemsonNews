package edu.cpsc4820.bhglove.simplenewsreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Prompts the user for internet permissions, automatically moves to either the login screen or
 * the main screen.
 * Created by Benjamin Glover 02/03/2016
 *
 * Resources:
 *
 * Quickly override listview font
 * http://stackoverflow.com/questions/4533440/android-listview-text-color
 *
 * Override Back Button
 *  - Our TA Sean
 * http://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
 *
 */
public class MainActivity extends AppCompatActivity{
    private final int MY_PERMISSIONS_REQUEST_INTERNET = 0;
    private int permissionCheck = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);
            }
        }
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Intent intent = new Intent(MainActivity.this, NewsFeed.class);
            startActivity(intent);
        }
        else{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {

                }
                return;
            }

        }
        // permissions this app might request
    }

}
