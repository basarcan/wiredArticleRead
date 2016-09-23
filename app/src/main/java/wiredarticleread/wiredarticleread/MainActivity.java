package wiredarticleread.wiredarticleread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    public ListView mListView;
    public TextView mTextView;
    public ArrayAdapter mArrayAdapter;
    public ArrayList<String> tagList;
    public ArrayList<String> linkList;
    public ArrayList<String> articleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.myTextView);


        requestData("https://www.wired.com/wp-json/wp/v2/posts/");

    }

    protected boolean isOnline() {  //çok önemli bir kod. internete bağlı olup olmadığını kontrol etmekte.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result){
            updateDisplay(result);
        }
    }

    private void requestData(String uri){
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String message){


        tagList = new ArrayList<String>();
        linkList = new ArrayList<String>();
        articleList = new ArrayList<String>();


        //Parse JSON display in listview
        try{
            JSONArray rootJSON = new JSONArray(message);

            for(int i = 0; i<5 ; i++)
            {
                JSONObject articleValuesJSON = rootJSON.getJSONObject(i);
                JSONObject titleJSON = articleValuesJSON.getJSONObject("title");
                String rendered = titleJSON.getString("rendered");
                tagList.add(Html.fromHtml(rendered).toString());
                linkList.add(articleValuesJSON.getString("link"));

                JSONObject contentJSON = articleValuesJSON.getJSONObject("content");
                articleList.add((Html.fromHtml(contentJSON.getString("rendered")).toString()));




            }
            mListView = (ListView) findViewById(R.id.myListView);
            mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tagList);
            mListView.setAdapter(mArrayAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public  void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                    Intent intent = new Intent(MainActivity.this,ReadArticle.class);
                    intent.putExtra("articleLink", linkList.get(i));
                    intent.putExtra("article", articleList.get(i));
                    startActivity(intent);
                }
            });

        }catch(JSONException e){
            e.printStackTrace();
        }


    }





}