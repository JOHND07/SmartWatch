package com.guogee.smartwatch.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Tools class ,provider some operation (such as copy assert to sd,visit
 * websize)
 * 
 * @author Huihan
 * 
 */
public class Util {
	
	public final static String TAG = "Util";
	public final static int HEADER_FONT = 0;
	public final static int BODY_FONT   = 1;
	public final static int BUTTON_FONT = 2;
	
	private final static String FRUTIGER_BOLD_PATH = "fonts/Frutiger-Bold.otf";
	private final static String FRUTIER_LIGHT_PATH = "fonts/Frutiger-Light.otf";
	private final static String ANDALE_MONO_PATH  = "fonts/Andale Mono.ttf";
	
	private final static String ANTONIO_LIGHT_PATH  = "fonts/ANTONIO-LIGHT.TTF";
	private final static String ANTONIO_BOLD_PATH  = "fonts/ANTONIO-BOLD.TTF";
	private final static String ANTONIO_REGULAR_PATH  = "fonts/ANTONIO-REGULAR.TTF";
	private final static String BEBASNEUE_PATH  = "fonts/BEBASNEUE2.OTF";
	
	
	private final static String BEBASNEUE  = "fonts/BebasNeue.otf";
	
	
	private static Typeface mHeaderFont;
	private static Typeface mBodyFont;
	private static Typeface mButtonFont;
	
	private static Typeface mPlusdotFont;
	private static Typeface mDateFont;

	private static void initialiseFonts(final Context context) {
//		mHeaderFont = Typeface.createFromAsset(context.getAssets(), FRUTIGER_BOLD_PATH);
		
		mPlusdotFont = Typeface.createFromAsset(context.getAssets(), ANTONIO_REGULAR_PATH);
		mDateFont = Typeface.createFromAsset(context.getAssets(), BEBASNEUE_PATH);
		
		mHeaderFont = Typeface.createFromAsset(context.getAssets(), BEBASNEUE);
		mBodyFont = Typeface.createFromAsset(context.getAssets(), FRUTIER_LIGHT_PATH);
		mButtonFont = Typeface.createFromAsset(context.getAssets(), ANDALE_MONO_PATH);
	}
	
	
	/**
	 * Set custom typefaces to a Textview or any view that contains a TextView / Button, 
	 * this method also handles what font to assign to each TextView / Button.
	 * 
	 * @param Context
	 * @param View - can be an individual TextView / Button or the whole view hierarchy containing TextViews / Buttons.
	 */
	public static Typeface overrideViewFonts(final Context context, final View v) {
		
		// Initialise Typeface
		if((mHeaderFont == null) || (mBodyFont == null) || (mButtonFont == null)){
			initialiseFonts(context);
		}
		
		Typeface tf = mHeaderFont;
		
		return tf;
	}
	
	
    public static Typeface DateTitleFonts(final Context context, final View v) {
    	
		if(mDateFont == null){
			initialiseFonts(context);
		}
		Typeface tf = mDateFont;
		return tf;
	}
	
    public static Typeface plusdotTitleFonts(final Context context, final View v) {
		
		if(mPlusdotFont == null){
			initialiseFonts(context);
		}
		Typeface tf = mPlusdotFont;
		return tf;
	}
	
	
	/**
	 * Set custom typeface to an android.graphics.Paint
	 * 
	 * @param Context 
	 * @param Paint to set typeface to
	 * @param int font - use Util constants: HEADER_FONT, BODY_FONT, BUTTON_FONT
	 */
	public static Paint overridePaintFonts(Context context, Paint paint){
		
		// Initialise Typeface
		if((mHeaderFont == null) || (mBodyFont == null) || (mButtonFont == null)){
			initialiseFonts(context);
		}
		
		paint.setTypeface(mHeaderFont);
		
		return paint;
		
//		switch(font){
//			case HEADER_FONT:
//				paint.setTypeface(mHeaderFont);
//				break;
//			case BODY_FONT:
//				paint.setTypeface(mBodyFont);
//				break;
//			case BUTTON_FONT:
//				paint.setTypeface(mButtonFont);
//				break;
//		}
	}
}
