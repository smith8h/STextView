# ExpandTextView 📖
[![](https://jitpack.io/v/smith8h/ExpandTextView.svg)](https://jitpack.io/#smith8h/ExpandTextView)
[![Build Status](https://travis-ci.org/niltonvasques/simplecov-shields-badge.svg?branch=master)](https://travis-ci.org/niltonvasques/simplecov-shields-badge)
![stability-stable](https://img.shields.io/badge/stability-stable-green.svg)
![minimumSDK](https://img.shields.io/badge/minSDK-21-f39f37)
![stable version](https://img.shields.io/badge/stable_version-v1.5-blue)
![Repository size](https://img.shields.io/github/repo-size/smith8h/expandtextview)
<br/>

**(Expandable TextView)** A TextView that use *Show More* and *Show Less* toggle button at the end of the text like Instagram, Facebook... etc.

<br/>

**Content**
- [**Setup 📲**](#setup-)
- [**Documentation 📃**](#documentation-)
  - [**XML Implementation**](#XML-Implementation)
  - [**Java Implementation**](#Java-Implementation)
  - [**Markdown Usage**](#markdown-text)
- [**Donations :heart:**](#donations-)
<br/>
<p align="center">
<img src="https://te.legra.ph/file/da8f1b95c7c4a842f3294.jpg" style="width: 83%;"/><br/>
</p>
<br/>

___
# Setup 📲
> **Step 1.** Add the JitPack repository to your build file.</br>
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```
> **Step 2.** Add the dependency:
```gradle
dependencies {
    implementation 'com.github.smith8h:ExpandTextView:v1.5'
}
```

<br/>

# Documentation 📃
### XML Implementation
> Implement ExpandTextView directly in your layout:
```xml
<smith.lib.views.expandtextview.ExpandTextView
    android:layout_height="wrap_content"
    android:layout_width="wrap_content"
    android:text="long dummy text..."
    android:textSize="16sp"
    android:textColor="#000000" />
```
**Default values**
- `expandTextColor` (optinal) set color to toggle button at the end of text and has a default color *#999999*.
- `maxToExpand` (optional) set max chars can be displayed when the view is collapsed, and has a default value **110**.
- `expanded` (optional) toggle the text, and default value is **false**.
- `expandText` & `collapseText` set toggle button text on expand and collapse, and default values are **More, Less**.
- `expandTextSize` (optional) set expand/collapse button relative text size, default value **0.9**.
- `enableExpands` (optional) enable/disable expand button, default value is **false** (disabled).
> - ⛔ `mentionsColor` is useless now! (planned to be used in next releases).
<br/>

___
### Java Implementation
> Implement ExpandTextView in Java:
```java
ExpandTextView etv = new ExpandTextView(context);

// default 110
etv.setMaxToExpand(60);
// default #999999
etv.setExpandTextColor(Color.BLACK);
// default (More, Less)
etv.setExpandTexts("See More", "See Less");
// default (false)
etv.setExpandsEnabled(true);
// default 0.9f
etv.setExpandTextSize(1f);
// set text
etv.setContentText("some dummy long text...");

// expand/collapse or toggle the opposite situation when needed
etv.setExpanded(false);
etv.toggle();
```
> - ⛔ Important Note: Do not use `setExpanded()`/`toggle()` before setting text with `setContentText("..")`. You can use them, if you already implemented it in your xml layout. 
> - ⛔ Note: `setContentText("..")` is the main method used to set the content text.
> - ⛔ `setMentionsClickListener` is useless now! (planned to be used in next releases).
<br/>

**Getters**:
```java
// check if it's expanded
if(etv.isExpanded()) {
    // DO STUFF
}

// get the original text 
String text = etv.getOriginalText();
```
**Set a listener (onClick/onLongClick)**:
```java
etv.setTextClickListener(new TextClickListener() {
    @Override public void onClick() {
        etv.toggle();
        Toast.makeText(context, etv.getOriginalText(), Toast.LENGTH_SHORT).show();
    }
    @Override public void onLongClick() {}
});
```

<br/>

___
### Markdown Text
Available markdown symbols:
```
**for bold text**
__for italic text__
*_for bold italic text_*
_*for bold italic text*_
`for monospace text that can be copied when clicked`
~~for strike through text~~
```

<br/>

___
# Donations ❤
> If you would like to support this project's further development, the creator of this projects or the continuous maintenance of the project **feel free to donate**.
Your donation is highly appreciated. Thank you!
<br/>

You can **choose what you want to donate**, all donations are awesome!</br>
<br/>

[![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.me/husseinshakir)
[![Buy me a coffee](https://img.shields.io/badge/Buy_Me_A_Coffee-FFDD00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://www.buymeacoffee.com/HusseinShakir)
[![Ko-fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/husseinsmith)
<br/>

<p align="center">
  <img src="https://raw.githubusercontent.com/smith8h/smith8h/main/20221103_150053.png" style="width: 38%;"/>
  <br><b>With :heart:</b>
</p>
