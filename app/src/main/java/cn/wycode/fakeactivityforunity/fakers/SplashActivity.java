package cn.wycode.fakeactivityforunity.fakers;

import android.os.Bundle;

import cn.wycode.fakeactivityforunity.Constants;
import cn.wycode.fakeactivityforunity.FakeActivity;
import cn.wycode.fakeactivityforunity.R;


/**
 * Created by wayne on 2017/12/25.
 */

public class SplashActivity extends FakeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onUnityMessage(String[] args) {
        if (args[0].equals(Constants.UNITY_MESSAGE_SCENE_LOADED) && args[1].equals(Constants.UNITY_SCENE_MAIN)) {
            finish();
            startActivity(MainActivity.class);
        }
    }
}
