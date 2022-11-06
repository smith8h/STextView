package smith.lib.views.expandtextview;

public interface TextClickListener {
    public void onClick(String originalText, boolean isExpanded);
    public void onLongClick(String originalText, boolean isExpanded);
}
