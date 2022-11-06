package smith.lib.views.expandtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.View;
import android.graphics.Typeface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import smith.lib.views.expandtextview.ClickListener;

public class ExpandTextView extends TextView {
	
	// >>>>>>>> variables
	private int textMaxLength = 110;
	private int textColor = 0xFF999999;
    private String showMoreText = "More", showLessText = "Less";
	private boolean isExpanded = false;
    private float expandBtnSize = 0.9f;
    private String expandedText, collapsedText, originalText;
	private SpannableString expandedTextSpannable, collapsedTextSpannable, expandedFinalSpannable, collapsedFinalSpannable;
	private ClickListener listener;
	
    
    
    
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
    
    
    
	
	// >>>>>>>> init attrs
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
	public void setClickListener(ClickListener listener) {
		this.listener = listener;
	}
	
	public void setMaxToExpand(int maxLength) {
		textMaxLength = maxLength;
	}
	
	public void setExpandTextColor(int color) {
		textColor = color;
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
	
	public void setExpandTexts(String showMore, String showLess) {
		showMoreText = showMore;
		showLessText = showLess;
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
		
        // show see more
		if (originalText.length() >= textMaxLength) {
			collapsedText = originalText.substring(0, textMaxLength) + "... " + showMoreText;
			expandedText = originalText + " " + showLessText;
			
			// creating btns spannable strings
			collapsedTextSpannable = new SpannableString(collapsedText);
			expandedTextSpannable = new SpannableString(expandedText);
			
			collapsedTextSpannable.setSpan(expandButtonsClickSpan, textMaxLength + 4, collapsedText.length(), 0);
			collapsedTextSpannable.setSpan(new StyleSpan(Typeface.BOLD), textMaxLength + 4, collapsedText.length(), 0);
			collapsedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), textMaxLength + 4, collapsedText.length(), 0);
			
			expandedTextSpannable.setSpan(expandButtonsClickSpan, originalText.length() + 1, expandedText.length(), 0);
			expandedTextSpannable.setSpan(new StyleSpan(Typeface.BOLD), originalText.length() + 1, expandedText.length(), 0);
			expandedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), originalText.length() + 1, expandedText.length(), 0);
			
			if (isExpanded) setText(expandedTextSpannable);
			else setText(collapsedTextSpannable);
		} else {
			setText(originalText);
		}
		
		setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if (listener != null) {
					if (getTag() == null || !getTag().equals("spanClicked")) {
						listener.onClick(originalText, isExpanded);
					}
				}
				setTag("textClicked");
			}
		});
		
		setOnLongClickListener(new OnLongClickListener() {
			@Override public boolean onLongClick(View v) {
				if (listener != null) {
					listener.onLongClick(originalText, isExpanded);
				}
				setTag("textLongClicked");
				return false;
			}
		});
	}
    
    
	
	ClickableSpan expandButtonsClickSpan = new ClickableSpan() {
		@Override public void onClick(View widget) {
			// to prevent toggle when long click on "show more/less"
			if (getTag() == null || !getTag().equals("textLongClicked")) {
				toggle();
				setTag("spanClicked");
			} else {
				setTag("");
			}
		}
		
		@Override public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
			ds.setColor(textColor);
		}
	};
}
