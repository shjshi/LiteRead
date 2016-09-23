package com.wenen.literead.activity.video;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.model.video.VideoModel;
import com.wenen.literead.utils.MD5;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class VideoPlayerActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_text)
    TextView tvText;
    private Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_video_player, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getVideo(String roomId) {
        subscriber = new Subscriber<VideoModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(VideoModel videoModel) {

            }
        };
        String time = System.currentTimeMillis() / 1000 + "";
        String commmon = "room/" + roomId + "?aid=android&cdn=ws&client_sys=android&time=" + time;
        String auth = MD5.strToMd5Low32(commmon + "1231");
        HttpClient.getSingle(APIUrl.DOUYU_API_URL).getVideo(roomId, "android", "ws", "android", time, auth, subscriber);
    }


}
