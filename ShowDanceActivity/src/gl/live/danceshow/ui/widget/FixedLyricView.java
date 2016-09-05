package gl.live.danceshow.ui.widget;

import gl.live.danceshow.ui.utils.Sentence;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author
 */
public class FixedLyricView extends TextView {
	private static final String TAG="FixedLyricView";
	
    private Paint NotCurrentPaint; // Not Cuurent Lyric Paint
    private Paint CurrentPaint; // Cuurent Lyric Paint
    private int notCurrentPaintColor = Color.WHITE;// Not Cuurent Lyric color of
                                                   // the Paint
    private int CurrentPaintColor = Color.RED; // Cuurent Lyric color of the
                                               // Paint
    private String mText;
    private Typeface Texttypeface = Typeface.SERIF;
    private Typeface CurrentTexttypeface = Typeface.DEFAULT_BOLD;
    private float width;
    private static Lyric mLyric;
    private int brackgroundcolor = 0xf000000; // Brackground color
    private float lrcTextSize = 22; // Lyric Text Size
    private float CurrentTextSize = 24;
    public float mTouchHistoryY;
	 private GestureDetector mDetector ; 
    private int height;
    private long currentDunringTime; // The duration of the current line of
                                     // lyrics, use the time to sleep
    private int TextHeight = 50; // Each row spacing
    private boolean lrcInitDone = false;// Is initialised.
    public int index = 0;
    private int lastIndex = 0;
    private List<Sentence> Sentencelist; // Lyric List

    private long currentTime;

    private long sentenctTime;

    public Paint getNotCurrentPaint() {
        return NotCurrentPaint;
    }

    public void setNotCurrentPaint(Paint notCurrentPaint) {
        NotCurrentPaint = notCurrentPaint;
    }

    public boolean isLrcInitDone() {
        return lrcInitDone;
    }

    public Typeface getCurrentTexttypeface() {
        return CurrentTexttypeface;
    }

    public void setCurrentTexttypeface(Typeface currrentTexttypeface) {
        CurrentTexttypeface = currrentTexttypeface;
    }

    public void setLrcInitDone(boolean lrcInitDone) {
        this.lrcInitDone = lrcInitDone;
    }

    public float getLrcTextSize() {
        return lrcTextSize;
    }

    public void setLrcTextSize(float lrcTextSize) {
        this.lrcTextSize = lrcTextSize;
    }

    public float getCurrentTextSize() {
        return CurrentTextSize;
    }

    public void setCurrentTextSize(float currentTextSize) {
        CurrentTextSize = currentTextSize;
    }

    public String getLyricText() {
    	return mText;
    }
    
    public static Lyric getmLyric() {
        return mLyric;
    }

    public void setmLyric(Lyric mLyric) {
        FixedLyricView.mLyric = mLyric;
    }

    public Paint getCurrentPaint() {
        return CurrentPaint;
    }

    public void setCurrentPaint(Paint currentPaint) {
        CurrentPaint = currentPaint;
    }

    public List<Sentence> getSentencelist() {
        return Sentencelist;
    }

    public void setSentencelist(List<Sentence> sentencelist) {
        Sentencelist = sentencelist;
    }

//    public int getNotCurrentPaintColor() {
//        return notCurrentPaintColor;
//    }
//
//    public void setNotCurrentPaintColor(int notCurrentPaintColor) {
//        this.notCurrentPaintColor = notCurrentPaintColor;
//    }
//
//    public int getCurrentPaintColor() {
//        return CurrentPaintColor;
//    }
//
//    public void setCurrentPaintColor(int currrentPaintColor) {
//        CurrentPaintColor = currrentPaintColor;
//    }
//
//    public Typeface getTexttypeface() {
//        return Texttypeface;
//    }
//
//    public void setTexttypeface(Typeface texttypeface) {
//        Texttypeface = texttypeface;
//    }
//
//    public int getBrackgroundcolor() {
//        return brackgroundcolor;
//    }
//
//    public void setBrackgroundcolor(int brackgroundcolor) {
//        this.brackgroundcolor = brackgroundcolor;
//    }
//
//    public int getTextHeight() {
//        return TextHeight;
//    }
//
//    public void setTextHeight(int textHeight) {
//        TextHeight = textHeight;
//    }

    public FixedLyricView(Context context) {
        super(context);
        init();
    }

    public FixedLyricView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public FixedLyricView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	Log.d(TAG," onTouchEvent ");
//    	return mDetector.onTouchEvent(event);
//    }
//    private float mPosX;
//    private float mPosY;
//    private float mCurrentPosX;
    private float mCurrentPosY;

	private void init() {
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				// 按下
				case MotionEvent.ACTION_DOWN:
//					mPosX = event.getX();
//					mPosY = event.getRawY();
					break;
				// 移动
				case MotionEvent.ACTION_MOVE:
					
//					mCurrentPosX = event.getX();
					mCurrentPosY = event.getRawY();
//					if(Math.abs(mCurrentPosX - mPosX) > 50) break;
//					if (mCurrentPosX - mPosX > 0
//							&& Math.abs(mCurrentPosY - mPosY) < 10)
//						Log.e(TAG, "向右");
//					else if (mCurrentPosX - mPosX < 0
//							&& Math.abs(mCurrentPosY - mPosY) < 10)
//						Log.e(TAG, "向左");
//					else 
					
					RelativeLayout parLayout = (RelativeLayout)getParent();
					if(mCurrentPosY >= (parLayout.getHeight() - getHeight()+20)
							||mCurrentPosY < (getHeight()+20)) break;
					
					RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)getLayoutParams();
					p.bottomMargin = parLayout.getHeight()-(int)mCurrentPosY - getHeight()/2;//p.bottomMargin-y;
//					Log.e(TAG, "p.bottomMargin " + p.bottomMargin);
					setLayoutParams(p);
						return true;
				// 拿起
				case MotionEvent.ACTION_UP:

					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	public float getTextRatio() {
		RelativeLayout parLayout = (RelativeLayout)getParent();
		RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)getLayoutParams();
		return p.bottomMargin*10f/(parLayout.getHeight()*10f);
		
	}
	
    /**
     * @param time
     *            The time axis current lyrics
     * @return null
     */
    public boolean updateIndex(long time) {

        this.currentTime = time;
        if (null == mLyric)
            return false;
        int in = mLyric.getNowSentenceIndex(time);
        if (in != -1 && in!= index) {
        	index = in;
            if (Sentencelist == null)
                return false;
            Sentence sen = Sentencelist.get(index);
            sentenctTime = sen.getFromTime();
            currentDunringTime = sen.getDuring();
            post(new Runnable() {
				@Override
				public void run() {
					Log.d("guolei","Sentencelist.get("+index+").getContent()	 "+Sentencelist.get(index).getContent()	);
					setText(Sentencelist.get(index).getContent()	);
				}
			});
            return true;
        }else {
        	return false;
        }
    }
}