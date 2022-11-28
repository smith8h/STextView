package smith.lib.views.tv;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.View;
import android.text.style.ClickableSpan;
import android.graphics.Typeface;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STextView extends TextView {
	
	// >>>>>>>> variables
    public static final int TYPE_MENTION = 0;
    public static final int TYPE_HASHTAG = 1;
    
	private int textMaxLength = 110;
	private int textColor = 0xFF999999;
    private int mentionsColor = 0xFF999999;
    
    private String showMoreText = "More";
    private String showLessText = "Less";
	private String expandedText, collapsedText, originalText;
	
    private boolean isExpanded = false;
    private boolean isEnabledExpands = false;
    
    private float expandBtnSize = 0.88f;
    
    private SpannableStringBuilder expandedTextSpannable, collapsedTextSpannable;
	
    private TextClickListener eListener;
	private MentionsClickListener mListener;
    
    private Context context;
    
    
    
    
	// >>>>>>>> constructors
	public STextView(Context context) {
		super(context);
		init(context, null);
        this.context = context;
		setContentText(getText().toString());
	}
	
	public STextView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
		if (attrs != null) {
			init(context, attrs);
		} else {
			setContentText(getText().toString());
		}
	}
	
	public STextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        this.context = context;
		if (attrs != null) {
			init(context, attrs);
		} else {
			setContentText(getText().toString());
		}
	}
    
	private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.STextView, 0, 0);
		try {
            // get max shown text from attributes
			int textLength = ta.getInteger(R.styleable.STextView_maxToExpand, textMaxLength);
			setMaxToExpand(textLength);
			
            // get expand text color from attributes
			String color = ta.getString(R.styleable.STextView_expandTextColor);
			if (!color.equals(null) || !color.equals("")) {
				setExpandTextColor(Color.parseColor(color));
			} else {
				setExpandTextColor(textColor);
			}
			
            // get expand texts from attributes
			String moreText = ta.getString(R.styleable.STextView_expandText);
			String lessText = ta.getString(R.styleable.STextView_collapseText);
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
            float size = ta.getFloat(R.styleable.STextView_expandTextSize, expandBtnSize);
			setExpandTextSize(size);
            
            // get enableability is on/off from attributes
			boolean clickable = ta.getBoolean(R.styleable.STextView_enableExpands, isEnabledExpands);
			setExpandsEnabled(clickable);
            
            // get mentions text color from attributes
			String mColor = ta.getString(R.styleable.STextView_mentionsColor);
			if (!mColor.equals(null) || !mColor.equals("")) {
				setMentionsColor(Color.parseColor(mColor));
			} else {
				setMentionsColor(mentionsColor);
			}
            
            // get and update text 
            String update = getText().toString();
			setContentText(update);
			
            // get expand is on/off from attributes
			boolean expanded = ta.getBoolean(R.styleable.STextView_expanded, isExpanded);
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
    
    public int getMaxToExpand() {
        return textMaxLength;
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
    
    public void setExpandsEnabled(boolean clickable) {
        isEnabledExpands = clickable;
    }
    
    public boolean isExpandsEnabled() {
        return isEnabledExpands;
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
		
		if (originalText.length() >= textMaxLength && isEnabledExpands) {
			collapsedText = originalText.substring(0, textMaxLength) + "... " + showMoreText;
			expandedText = originalText + " " + showLessText;
			
			collapsedTextSpannable = new SpannableStringBuilder(collapsedText);
			collapsedTextSpannable.setSpan(expandButtonsClickSpan, textMaxLength + 4, collapsedText.length(), 0);
			collapsedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), textMaxLength + 4, collapsedText.length(), 0);
			setMarkdownSpans(collapsedTextSpannable, collapsedText);
            
            expandedTextSpannable = new SpannableStringBuilder(expandedText);
			expandedTextSpannable.setSpan(expandButtonsClickSpan, originalText.length() + 1, expandedText.length(), 0);
			expandedTextSpannable.setSpan(new RelativeSizeSpan(expandBtnSize), originalText.length() + 1, expandedText.length(), 0);
			setMarkdownSpans(expandedTextSpannable, expandedText);
            
			if (isExpanded) setText(expandedTextSpannable);
			else setText(collapsedTextSpannable);
		} else {
            SpannableStringBuilder ss = new SpannableStringBuilder(originalText);
            setMarkdownSpans(ss, originalText);
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
        List<StyleSpan> spans = new ArrayList<>();
        List<TypefaceSpan> spans2 = new ArrayList<>();
        List<StrikethroughSpan> spans3 = new ArrayList<>();
        List<UnderlineSpan> spans4 = new ArrayList<>();
        List<SpoilerClickSpan> spans5 = new ArrayList<>();
        
        // ** bold **
        Pattern p = Pattern.compile("(\\*\\*)(.*?)(\\*\\*)");
        Matcher matcher = p.matcher(text);
        while (matcher.find()) { markdown(spans, ssb, matcher, Typeface.BOLD); }
        
        // -- underline --
        p = Pattern.compile("(\\-\\-)(.*?)(\\-\\-)");
        matcher = p.matcher(text);
        while (matcher.find()) {
            UnderlineSpan us = new UnderlineSpan();
            ssb.setSpan(us, matcher.start(), matcher.end(), 0);
            spans4.add(us);
        }
        
        // __ italic __
        p = Pattern.compile("(\\_\\_)(.*?)(\\_\\_)");
        matcher = p.matcher(text);
        while (matcher.find()) { markdown(spans, ssb, matcher, Typeface.ITALIC); }
        
        // ` monospace `
        p = Pattern.compile("(\\`)(.*?)(\\`)");
        matcher = p.matcher(text);
        while (matcher.find()) { 
            TypefaceSpan span = new TypefaceSpan(Typeface.MONOSPACE);
            MonospaceClickSpan cSpan = new MonospaceClickSpan();
            ssb.setSpan(span, matcher.start(), matcher.end(), 0);
            ssb.setSpan(cSpan, matcher.start(), matcher.end(), 0);
            spans2.add(span);
        }
        
        // ~~ strike through ~~
        p = Pattern.compile("(\\~\\~)(.*?)(\\~\\~)");
        matcher = p.matcher(text);
        while (matcher.find()) { 
            StrikethroughSpan span = new StrikethroughSpan();
            ssb.setSpan(span, matcher.start(), matcher.end(), 0);
            spans3.add(span);
        }
        
        // spoiler
        p = Pattern.compile("(\\|\\|)(.*?)(\\|\\|)");
        matcher = p.matcher(text);
        while (matcher.find()) {
            BackgroundColorSpan bgs = new BackgroundColorSpan(textColor);
            ForegroundColorSpan fgs = new ForegroundColorSpan(textColor);
            int start = matcher.start();
            int end = matcher.end() - 4;
            SpoilerClickSpan scs = new SpoilerClickSpan(bgs, fgs, this, start, end);
            ssb.setSpan(scs, matcher.start(), matcher.end(), 0);
            ssb.setSpan(bgs, matcher.start(), matcher.end(), 0);
            ssb.setSpan(fgs, matcher.start(), matcher.end(), 0);
            spans5.add(scs);
        }
        
        // delete **__~~``--||
        for (StyleSpan span : spans) {
	        ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 2, "");
	        ssb.replace(ssb.getSpanEnd(span) - 2, ssb.getSpanEnd(span), "");
	    }
        for (TypefaceSpan span : spans2) {
            ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 1, "");
	        ssb.replace(ssb.getSpanEnd(span) - 1, ssb.getSpanEnd(span), "");
        }
        for (StrikethroughSpan span : spans3) {
            ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 2, "");
	        ssb.replace(ssb.getSpanEnd(span) - 2, ssb.getSpanEnd(span), "");
        }
        for (UnderlineSpan span : spans4) {
            ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 2, "");
	        ssb.replace(ssb.getSpanEnd(span) - 2, ssb.getSpanEnd(span), "");
        }
        for (SpoilerClickSpan span : spans5) {
            ssb.replace(ssb.getSpanStart(span), ssb.getSpanStart(span) + 2, "");
            ssb.replace(ssb.getSpanEnd(span) - 2, ssb.getSpanEnd(span), "");
        }
        
        // mentions & hashtags
        p = Pattern.compile("(?<![^\\s])(([@]{1}|[#]{1})([A-Za-z0-9_-]\\.?)+)(?![^\\s,])");
		matcher = p.matcher(ssb);
		while(matcher.find()){
            ProfileSpan ps = new ProfileSpan();
            ssb.setSpan(ps, matcher.start(), matcher.end(), 0);
        }
    }
    
    private void markdown(List<StyleSpan> spans, SpannableStringBuilder ssb, Matcher matcher, int mark) {
        StyleSpan span = new StyleSpan(mark);
	    ssb.setSpan(span, matcher.start(), matcher.end(), 0);
        spans.add(span);
    }
    
    private class SpoilerClickSpan extends ClickableSpan {
        
        BackgroundColorSpan bgs;
        ForegroundColorSpan fgs;
        STextView stv;
        int start, end;
        
        SpoilerClickSpan(BackgroundColorSpan bgs, ForegroundColorSpan fgs, STextView stv, int start, int end) {
            this.bgs = bgs;
            this.fgs = fgs;
            this.stv = stv;
            this.start = start;
            this.end = end;
        }
        
        @Override public void onClick(View v) {
            if (((TextView)v).getText() instanceof SpannableString) {
                SpannableString ssb = (SpannableString)((TextView)v).getText();
                
                ssb.removeSpan(bgs);
                ssb.removeSpan(bgs);
                ssb.removeSpan(this);
                
                fgs = new ForegroundColorSpan(stv.getCurrentTextColor());
                ssb.setSpan(fgs, start, end, 0);
                setText(ssb);
            }
        }
        
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(stv.getCurrentTextColor());
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
                    if (mListener != null) {
                        if (sp.subSequence(start, start+1).equals("@")) mListener.onClick(sp.subSequence(start,end).toString(), TYPE_MENTION);
                        else mListener.onClick(sp.subSequence(start,end).toString(), TYPE_HASHTAG);
                    }
				}
			}
		}
        
		@Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
	        ds.setColor(mentionsColor);
			ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
	    }
	}
    
    private class MonospaceClickSpan extends ClickableSpan {
        @Override public void onClick(View view){
			if(view instanceof TextView){
				TextView tv = (TextView)view;
			    if(tv.getText() instanceof Spannable){
					Spannable sp = (Spannable)tv.getText();
					int start = sp.getSpanStart(this);
				    int end = sp.getSpanEnd(this);
                    String text = sp.subSequence(start, end).toString();
                    ((ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE))
                            .setPrimaryClip(ClipData.newPlainText("clipboard", text));
                    Toast.makeText(context, context.getString(R.string.copied_span_click), Toast.LENGTH_SHORT).show();
				}
			}
		}
        
		@Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(textColor);
            ds.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
	    }
    }
	
	ClickableSpan expandButtonsClickSpan = new ClickableSpan() {
		@Override public void onClick(View v) {
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
