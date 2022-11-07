package smith.lib.views.expandtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.View;
import android.text.style.ClickableSpan;
import android.graphics.Typeface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandTextView extends TextView {
	
	// >>>>>>>> variables
	private int textMaxLength = 110;
	private int textColor = 0xFF999999;
    private int mentionsColor = 0xFF999999;
    private String showMoreText = "More", showLessText = "Less";
	private boolean isExpanded = false;
    private boolean isClickableMentions = false;
    private float expandBtnSize = 0.9f;
    private String expandedText, collapsedText, originalText;
	private SpannableStringBuilder expandedTextSpannable, collapsedTextSpannable, expandedFinalSpannable, collapsedFinalSpannable;
	private TextClickListener eListener;
	private MentionsClickListener mListener;
    
    
    
    
	// >>>>>>>> constructors
	public ExpandTextView(Context context) {
		super(context);
		init(context, null);
		setContentText(getText().toString());
	}
	
	public ExpandTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs != null) {
			init(context, attrs);
		} else {
			setContentText(getText().toString());
		}
	}
	
	public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (attrs != null) {
			init(context, attrs);
		} else {
			setContentText(getText().toString());
		}
	}
    
	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0);
		try {
            // get max shown text from attributes
			int textLength = ta.getInteger(R.styleable.ExpandTextView_maxToExpand, textMaxLength);
			setMaxToExpand(textLength);
			
            // get expand text color from attributes
			String color = ta.getString(R.styleable.ExpandTextView_expandTextColor);
			if (!color.equals(null) || !color.equals("")) {
				setExpandTextColor(Color.parseColor(color));
			} else {
				setExpandTextColor(textColor);
			}
			
            // get expand texts from attributes
			String moreText = ta.getString(R.styleable.ExpandTextView_expandText);
			String lessText = ta.getString(R.styleable.ExpandTextView_collapseText);
			if (!moreText.equals(null) && !lessText.equals(null)) {
				setExpandTexts(moreText, lessText);
			} else if (!moreText.equals(null) && lessText.equals(null)) {
				setExpandTexts(moreText, showLessText);
			} else if (moreText.equals(null) && !lessText.equals(null)) {
				setExpandTexts(showMoreText, lessText);
			} else {
				setExpandTexts(showLessText, showLessText);
			}
            
            // get expand text size from attributes
            float size = ta.getFloat(R.styleable.ExpandTextView_expandTextSize, expandBtnSize);
			setExpandTextSize(size);
            
            // get mentions is on/off from attributes
			boolean clickable = ta.getBoolean(R.styleable.ExpandTextView_clickableMentions, isClickableMentions);
			setClickableMentions(clickable);
            
            // get mentions text color from attributes
			String mColor = ta.getString(R.styleable.ExpandTextView_mentionsColor);
			if (!mColor.equals(null) || !mColor.equals("")) {
				setMentionsColor(Color.parseColor(mColor));
			} else {
				setMentionsColor(mentionsColor);
			}
            
            // get and update text 
            String update = getText().toString();
			setContentText(update);
			
            // get expand is on/off from attributes
			boolean expanded = ta.getBoolean(R.styleable.ExpandTextView_expanded, isExpanded);
			setExpanded(expanded);
		} catch (Exception e) { e.printStackTrace(); }
		ta.recycle();
	}
    
    
    
	
	// >>>>>>>> methods
	public void setTextClickListener(TextClickListener listener) {
		eListener = listener;
	}
    
    public void setMentionsClickListener(MentionsClickListener listener) {
        mListener = listener;
    }
	
	public void setMaxToExpand(int maxLength) {
		textMaxLength = maxLength;
	}
	
	public void setExpandTextColor(int color) {
		textColor = color;
	}
    
    public void setMentionsColor(int color) {
        mentionsColor = color;
    }
	
	public void setExpandTexts(String showMore, String showLess) {
		showMoreText = showMore;
		showLessText = showLess;
	}
    
    public void setClickableMentions(boolean clickable) {
        isClickableMentions = clickable;
    }
    
    public void setExpanded(boolean expand) {
		if (expand) {
			isExpanded = true;
			setText(expandedTextSpannable);
		} else {
			isExpanded = false;
			setText(collapsedTextSpannable);
		}
	}
	
	public boolean isExpanded() {
		return isExpanded;
	}
	
	public String getOriginalText() {
		return originalText;
	}
	
	public void toggle() {
		if (isExpanded) {
			isExpanded = false;
			setText(collapsedTextSpannable);
		} else {
			isExpanded = true;
			setText(expandedTextSpannable);
		}
	}
    
    public void setExpandTextSize(float size) {
        expandBtnSize = size;
    }
    
	public void setContentText(String text) {
		originalText = text;
		this.setMovementMethod(LinkMovementMethod.getInstance());
		
		if (originalText.length() >= textMaxLength) {
			collapsedText = originalText.substring(0, textMaxLength) + "... " + showMoreText;
			expandedText = originalText + " " + showLessText;
			
			collapsedTextSpannable = new SpannableStringBuilder(collapsedText);
			collapsedTextSpannable.setSpan(expandButtonsClickSpan, textMaxLength + 4, collapsedText.length(), 0);
			collapsedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), textMaxLength + 4, collapsedText.length(), 0);
			setMarkdownSpans(collapsedTextSpannable, collapsedText);
            if (isClickableMentions) setMentionsSpan(collapsedTextSpannable, collapsedText);
            
            expandedTextSpannable = new SpannableStringBuilder(expandedText);
			expandedTextSpannable.setSpan(expandButtonsClickSpan, originalText.length() + 1, expandedText.length(), 0);
			expandedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), originalText.length() + 1, expandedText.length(), 0);
			setMarkdownSpans(expandedTextSpannable, expandedText);
            if (isClickableMentions) setMentionsSpan(expandedTextSpannable, expandedText);
            
			if (isExpanded) setText(expandedTextSpannable);
			else setText(collapsedTextSpannable);
		} else {
            SpannableStringBuilder ss = new SpannableStringBuilder(originalText);
            setMarkdownSpans(ss, originalText);
            if (isClickableMentions) setMentionsSpan(ss, originalText);
        	setText(ss);
		}
		
		setOnClickListener(v -> {
			if (getTag() == null || !getTag().equals("spanClicked")) {
				if (eListener != null) eListener.onClick(originalText, isExpanded);
            } setTag("textClicked");
		});
		setOnLongClickListener(v -> {
			if (eListener != null) eListener.onLongClick(originalText, isExpanded);
			setTag("textLongClicked");
			return false;
		});
	}
    
    
    
    
    // >>>>>>>>>> extras
    private void setMarkdownSpans(SpannableStringBuilder ssb, String text) {
        // ** bold **
        Pattern p = Pattern.compile("(\\*\\*)(.*?)(\\*\\*)");
        Matcher matcher = p.matcher(text);

        List<StyleSpan> spans = new ArrayList<>();
        //making text bold
        while (matcher.find()) {
	        StyleSpan span = new StyleSpan(Typeface.BOLD);
	        ssb.setSpan(span, matcher.start(), matcher.end(), 0);
	        spans.add(span);
	    }
        for (StyleSpan span : spans) {
	        ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 2, "");
	        ssb.replace(ssb.getSpanEnd(span) - 2, ssb.getSpanEnd(span), "");
	    }
    }
    
    private void setMentionsSpan(SpannableStringBuilder ssb, String str){
		Pattern pattern = Pattern.compile("(?<![^\\s])(([@]{1}|[#]{1})([A-Za-z0-9_-]\\.?)+)(?![^\\s,])");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()){
            ProfileSpan mentionsClickSpan = new ProfileSpan();
			ssb.setSpan(mentionsClickSpan, matcher.start()-4, matcher.end()-4, 0);
		}
	}
    
    private class ProfileSpan extends ClickableSpan {
		@Override public void onClick(View view){
			if(view instanceof TextView){
				TextView tv = (TextView)view;
			    if(tv.getText() instanceof Spannable){
					Spannable sp = (Spannable)tv.getText();
					int start = sp.getSpanStart(this);
				    int end = sp.getSpanEnd(this);
                    if (mListener != null) mListener.onClick(sp.subSequence(start,end).toString());
				}
			}
		}
        
		@Override public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
	        ds.setColor(mentionsColor);
			ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
	    }
	}
	
	ClickableSpan expandButtonsClickSpan = new ClickableSpan() {
		@Override public void onClick(View widget) {
			if (getTag() == null || !getTag().equals("textLongClicked")) {
				toggle(); setTag("spanClicked");
			} else setTag("");
		}
		
		@Override public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
			ds.setColor(textColor);
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		}
	};
}
