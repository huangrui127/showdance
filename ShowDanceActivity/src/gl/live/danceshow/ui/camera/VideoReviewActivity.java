package gl.live.danceshow.ui.camera;

import java.io.File;

import com.android.app.wumeiniang.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

/**
 * 录制视频回放页面
 * Created by zhengxiao on 14-2-15.
 */
public class VideoReviewActivity extends Activity implements View.OnClickListener {

    private VideoView videoView;
    @Nullable
    private Uri videoUri;

    public static void actionReview(@NonNull Activity activity, int request, Uri videoUri) {
        Intent intent = new Intent(activity, VideoReviewActivity.class);
        intent.setData(videoUri);
        activity.startActivityForResult(intent, request);
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_review_activity);
        videoView = (VideoView) findViewById(R.id.videoView);
        findViewById(R.id.buttonOK).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);

        videoUri = getIntent().getData();
        if (videoUri == null) finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
        videoView.start();
    }

    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.buttonOK) {
            Intent data = new Intent();
            data.setData(videoUri);
            setResult(RESULT_OK, data);
            finish();

        } else if (i == R.id.buttonCancel) {// remove file
            File file = new File(videoUri.getPath());
            if (file.exists()) {
                file.delete();
            }
            finish();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }
}