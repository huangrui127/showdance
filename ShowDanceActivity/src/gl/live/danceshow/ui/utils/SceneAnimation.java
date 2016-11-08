package gl.live.danceshow.ui.utils;

import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;

import android.os.Handler;

/**
 * 功能：多图加载形成动态图效果类
 * @author djd
 *
 */
public class SceneAnimation {
    private SimpleDraweeView mSdv;
    // private int[] mFrameRess; // 图片
    private int[] mDurations;
    private int mDuration;

    private List<String> mFrameRess; // 下载的图片本地绝对存储路径字符串集合

    private Handler handler = new Handler();
    private Runnable mRunnable;
    
    private int mLastFrameNo;
    private long mBreakDelay;

    public void changeFrameRess(SimpleDraweeView pSdv,List<String> pFrameRess, int pDuration){
        mSdv = pSdv;
        mFrameRess = pFrameRess;
        mDuration = pDuration;
        mLastFrameNo = pFrameRess.size() - 1;

        mSdv.setImageURI(pFrameRess.get(0));
        playConstant(1);
    }
    
    /**
     * 功能：根据传入的图片绝对地址数组，实现动态图效果
     * 
     * @param pSdv
     * @param pFrameRess 需要动态播放的图片绝对地址数组
     * @param pDurations 
     */
    public SceneAnimation(SimpleDraweeView pSdv, List<String> pFrameRess, int[] pDurations) {
        mSdv = pSdv;
        mFrameRess = pFrameRess;
        mDurations = pDurations;
        mLastFrameNo = pFrameRess.size() - 1;

        mSdv.setImageURI(pFrameRess.get(0));
        play(1);
    }

    // 参数包括一个int 的执行时间 pDuration， 执行循环播放playConstant(1);
    public SceneAnimation(SimpleDraweeView pSdv, List<String> pFrameRess, int pDuration) {
        mSdv = pSdv;
        mFrameRess = pFrameRess;
        mDuration = pDuration;
        mLastFrameNo = pFrameRess.size() - 1;

        mSdv.setImageURI(pFrameRess.get(0));
        playConstant(1);
    }

    // 参数包括一个int的pDuration，一个long的pBreakDelay，表示每次播放的间隔，执行循环播放playConstant(1);
    public SceneAnimation(SimpleDraweeView pSdv, List<String> pFrameRess, int pDuration, long pBreakDelay) {
        mSdv = pSdv;
        mFrameRess = pFrameRess;
        mDuration = pDuration;
        mLastFrameNo = pFrameRess.size() - 1;
        mBreakDelay = pBreakDelay;

        mSdv.setImageURI(pFrameRess.get(0));
        playConstant(1);
    }

    private void play(final int pFrameNo) {
        mRunnable = new Runnable() {
            public void run() {
                // mSdv.setBackgroundResource(mFrameRess[pFrameNo]);
                mSdv.setImageURI(mFrameRess.get(pFrameNo));
                if (pFrameNo == mLastFrameNo)
                    play(0);
                else
                    play(pFrameNo + 1);
            }
        };
        handler.postDelayed(mRunnable, mDurations[pFrameNo]);
    }

    private void playConstant(final int pFrameNo) {
        handler.postDelayed(new Runnable() {
            public void run() {
                mSdv.setImageURI(mFrameRess.get(pFrameNo));

                if (pFrameNo == mLastFrameNo)
                    playConstant(0);
                else
                    playConstant(pFrameNo + 1);
            }
        }, pFrameNo == mLastFrameNo && mBreakDelay > 0 ? mBreakDelay : mDuration);
    }

};
