package edu.cpsc4820.bhglove.simplenewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main activity that displays the a list of articles along with descriptive attributes.
 * Displays the description and title of all articles in a ListView
 * Created by Benjamin Glover 2/27/2016
 */
public class NewsFeed extends AppCompatActivity {
    private ListView mListView;
    private DataModel mData = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        if (mData == null)
            mData = DataModel.getInstance(getApplicationContext());

        Button categoryButton = (Button) findViewById(R.id.buttonAddCat);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFeed.this, Subscription.class);
                startActivity(intent);
            }
        });

        ImageButton infoButton = (ImageButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFeed.this, InfoActivity.class);
                startActivity(intent);
            }
        });



        //Handler to offset the download of RSS Content to another thread.
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final TextView downloading = (TextView) findViewById(R.id.downloadingText);
                downloading.setVisibility(View.VISIBLE);
                try {
                    mData.refreshDataContent();
                    while (mData.getProgress() < 98) {
                        Thread.sleep(500);
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloading.setVisibility(View.INVISIBLE);
                        createListView();
                    }
                });
            }
        });
        thread.start();
    }

    // Overrides the back button to set NewsFeed as the new Main Screen
    @Override
    public void onBackPressed() {
        if(mData.getProgress() < 98){
            Toast.makeText(getApplicationContext(), "Your articles is still downloading. Please Wait",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    /**
     * Populates a list view with the provided adapter from DataModel
     */
    private void createListView() {
        Log.d("NewsFeed", "Creating List View");
        mListView = (ListView) findViewById(R.id.newsFeedView);
        ArrayAdapter<String> adapter = mData.createNewsFeedAdapter(getApplicationContext());
        mListView.setAdapter(adapter);
        TextView empty = (TextView) findViewById(R.id.emptyNewsFeed);


        //Display a list of articles or a message identifying that there are no articles to show.
        if(adapter.isEmpty()){
            mListView.setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);

        }
        else{
            mListView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
        }

        progressBar.setVisibility(View.GONE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsFeed.this, ArticleActivity.class);

                intent.putExtra("Link", mData.getLinks().get(position).toString());

                startActivity(intent);
            }
        });
    }
}

