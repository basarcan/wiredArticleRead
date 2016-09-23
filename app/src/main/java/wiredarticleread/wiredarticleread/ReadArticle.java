package wiredarticleread.wiredarticleread;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;

import android.view.View;
import android.webkit.WebView;

import android.widget.Button;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ReadArticle extends AppCompatActivity {

    public WebView myWebView;
    public TextView myTextView;

    public ArrayList<String> usedWords = new ArrayList<String>();
    public ArrayList<String> wordsList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_article);



        /**                     !!-- PS --!!
         *    The mail is not clear about the article will shown how. The JSON data has article sites data and also
         *    the article so, i thought webview can handle nicely. However the article data comes with html phrases so some
         *    cases it can look odd. I comment lined the vewview way so either both way runs perfectly.
         *
         *                                                                                                                  **/
        Intent intent = getIntent();
        //String value = intent.getStringExtra("articleLink");
        //myWebView = (WebView) findViewById(R.id.articleView);
        //myWebView.loadUrl(value);


        myTextView = (TextView) findViewById(R.id.mView);
        myTextView.setMovementMethod(new ScrollingMovementMethod());
        final String getValue = intent.getStringExtra("article");
        myTextView.setText(getValue);

        checkString(getValue);


        final Button button = (Button) findViewById(R.id.myButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ReadArticle.this,TranslatedWords.class);
                intent.putExtra("translated",usedWords.get(0) + "   -   " + wordsList.get(0) +
                        usedWords.get(1) + "   -   " + wordsList.get(1) + usedWords.get(2) + "   -   " +  wordsList.get(2) +
                        usedWords.get(3) + "   -   " + wordsList.get(3) + usedWords.get(4) + "   -   " +  wordsList.get(4));
                startActivity(intent);


            }
        });
    }

    public void checkString (String article)
    {
        String textars = article;
        String textars1 = textars.toLowerCase();
        String string2[] = textars1.split(" ");
        HashMap<String, Integer> uniques = new HashMap<String, Integer>();
        for (String word : string2)
        {
            if(word.length() <= 2){ // am is are lardan kurtulmak için
                continue;
            }
            else if(word.equals("the")){
                continue;
            }

            Integer existingCount = uniques.get(word);
            uniques.put(word, (existingCount == null ? 1 : (existingCount + 1)));
        }

        uniques = sortByValues(uniques);

        Set<Map.Entry<String , Integer>> uniqueSet = uniques.entrySet();

        int i = 0;
        for (Map.Entry<String, Integer> entry : uniqueSet)
        {

            if(entry.getValue() > 1 && i < 5){
                usedWords.add(entry.getKey());
                i++;
            }
        }

        //myTextView.setText(usedWords.toString());

        requestData("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20160922T202256Z.1031e7e0f04bd371.c83a70e99abd43fb12d727e279591c6d6127856c&text=" + usedWords.get(0) + "&text=" + usedWords.get(1) + "&text=" + usedWords.get(2) + "&text=" + usedWords.get(3) + "&text=" + usedWords.get(4) + "&lang=en-tr");

    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result){
            getTranslatedLanguage(result);
        }
    }

    private void requestData(String uri){
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void getTranslatedLanguage(String message){


        //Parse JSON display in listview
        try{
            JSONObject rootJSON = new JSONObject(message);
            JSONArray textJSON = rootJSON.getJSONArray("text");

            for(int i = 0; i < 5; i++)
            {
                wordsList.add(textJSON.getString(i) + "\n");
            }

        }catch(JSONException e){
            e.printStackTrace();
        }


    }
}


