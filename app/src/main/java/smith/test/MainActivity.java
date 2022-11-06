package smith.test;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.itsaky.androidide.logsender.LogSender;
import smith.lib.views.expandtextview.ClickListener;
import smith.lib.views.expandtextview.ExpandTextView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        ExpandTextView etv = new ExpandTextView(this);
       
        etv.setClickListener(new ClickListener() {
            @Override
            public void onClick(String originalText, boolean isExpanded) {
                etv.toggle();
            }
            
            @Override
            public void onLongClick(String originalText, boolean isExpanded) {
                Toast.maleText(MainActivity.this, originalText, Toast.LENGTH_SHORT).show();
            }
        });
        
        etv.setMaxToExpand(110);
        etv.setExpandTextColor(getColor(R.color.acc));
        etv.setExpandTextSize(.8f);
        etv.setExpandTexts("Show More", "Show Less");
        etv.setContentText("EveryThingUtils library by @programmer_ameer\nvery useful library support Android 5-13\ntested &amp; working on android 10+\n• FileUtils\n• AudioUtils\n• PdfUtils\n• ImageUtils\n• ApkUtils\n\nThe best choice if you decide to create a File Manager App!\n\nCheck it here https://github.com/abodinagdat16/EveryThingUtils");
        
        etv.setExpanded(false);
        etv.toggle();
        
        // getters
        boolean isExpanded = etv.isExpanded();
        String originalText = etv.getOriginalText();
        
        
    }
    
}
