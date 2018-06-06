package cn.wycode.fakeactivityforunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.wycode.fakeactivityforunity.fakers.SplashActivity;


public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toUnity();
        finish();
    }


    public void toUnity() {
        Intent unityIntent = new Intent(this, UnityActivity.class);
        unityIntent.putExtra(Constants.EXTRA_KEY_UNITY_FIRST_FAKER, SplashActivity.class);
        startActivity(unityIntent);
    }
}
