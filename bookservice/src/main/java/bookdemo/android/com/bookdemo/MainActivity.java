package bookdemo.android.com.bookdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BookService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"try to start Service");
        Intent intent = new Intent(MainActivity.this,bookService.class);
        startService(intent);
    }
}
