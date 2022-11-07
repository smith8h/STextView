package smith.test;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.itsaky.androidide.logsender.LogSender;
import smith.lib.views.expandtextview.TextClickListener;
import smith.lib.views.expandtextview.ExpandTextView;
import smith.lib.views.expandtextview.MentionsClickListener;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        ExpandTextView etv = new ExpandTextView(this);
       
        etv.setTextClickListener(new TextClickListener() {
            @Override public void onClick(String originalText, boolean isExpanded) {}
            @Override public void onLongClick(String originalText, boolean isExpanded) {
                Toast.makeText(MainActivity.this, originalText, Toast.LENGTH_SHORT).show();
            }
        });
        
        etv.setMentionsClickListener(mention -> {
            Toast.makeText(MainActivity.this, mention, Toast.LENGTH_SHORT).show();
        });
        
        etv.setMaxToExpand(110);
        etv.setTextColor(getColor(R.color.txt));
        etv.setExpandTextColor(getColor(R.color.acc));
        etv.setExpandTextSize(.8f);
        etv.setExpandTexts("Show More", "Show Less");
        
        etv.setClickableMentions(true);
        etv.setMentionsColor(getColor(R.color.acc));
        
        etv.setContentText("**EveryThingUtils** library by @programmer_ameer\nvery useful library support Android 5-13\ntested &amp; working on android 10+\n• FileUtils\n• AudioUtils\n• PdfUtils\n• ImageUtils\n• ApkUtils\n\nThe best choice if you decide to create a #File_Manager App!\n\nCheck it here https://github.com/abodinagdat16/EveryThingUtils");
        
        ((LinearLayout)findViewById(R.id.main)).addView(etv);
    }
    
}
