package wiredarticleread.wiredarticleread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;


public class TranslatedWords extends AppCompatActivity {

    public TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        Intent intent = getIntent();
        final String getValue = intent.getStringExtra("translated");
        myTextView = (TextView) findViewById(R.id.popTextView);
        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);




        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        myTextView.setText(getValue);


    }


}
