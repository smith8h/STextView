package smith.test;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.itsaky.androidide.logsender.LogSender;
import smith.lib.views.tv.STextView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    
    public void check(View v) {
        EditText et = findViewById(R.id.et);
        STextView etv = findViewById(R.id.etv);
        
        etv.setContentText(et.getText().toString());
    }
}