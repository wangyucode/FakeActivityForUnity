package cn.wycode.fakeactivityforunity.fakers;

import android.os.Bundle;
import android.view.View;

import cn.wycode.fakeactivityforunity.FakeActivity;
import cn.wycode.fakeactivityforunity.R;
import cn.wycode.fakeactivityforunity.utils.UnityMessageUtil;

/**
 * Created by wayne on 2018/6/6.
 */

public class MainActivity extends FakeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onUnityMessage(String[] args) {
        super.onUnityMessage(args);
    }
}
