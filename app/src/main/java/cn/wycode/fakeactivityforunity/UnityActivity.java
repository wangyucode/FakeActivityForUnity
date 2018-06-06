package cn.wycode.fakeactivityforunity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

import java.util.Stack;

import cn.wycode.fakeactivityforunity.utils.LogUtil;

public class UnityActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    private FrameLayout unityContainer, fakeActivityContainer;


    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    private FakeActivity currentFakeActivity;

    private Stack<FakeActivity> fakeActivityStack;

    private View mainLoading;

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);

        unityContainer = findViewById(R.id.unity_container);
        fakeActivityContainer = findViewById(R.id.fake_activity_container);
        mainLoading = findViewById(R.id.main_loading);

        initUnity();

        fakeActivityStack = new Stack<>();

        initFirstFakeActivity(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    private void initFirstFakeActivity(Bundle savedInstanceState) {
        Class<? extends FakeActivity> fakeClass = (Class) getIntent().getSerializableExtra(Constants.EXTRA_KEY_UNITY_FIRST_FAKER);
        startActivity(fakeClass, savedInstanceState);
    }

    public void startActivity(Class<? extends FakeActivity> activityClass, Bundle savedInstanceState) {
        //TODO save state
        currentFakeActivity = null;
        try {
            currentFakeActivity = activityClass.newInstance();
            currentFakeActivity.mContext = this;
        } catch (Exception e) {
            LogUtil.e(this, "fake activity init error!");
            e.printStackTrace();
        }

        if (currentFakeActivity != null) {
            currentFakeActivity.onCreate(savedInstanceState);
            currentFakeActivity.onStart();
            currentFakeActivity.onResume();

            fakeActivityStack.push(currentFakeActivity);
        }

        LogUtil.i("push: fake stack size = " + fakeActivityStack.size());
    }

    public void fakeViewInflated(View fakeActivityView) {
        fakeActivityContainer.removeAllViews();
        fakeActivityContainer.addView(currentFakeActivity.fakeActivityView);
    }

    public void popFake() {
        fakeActivityContainer.removeAllViews();
        if (!fakeActivityStack.isEmpty()) {
            fakeActivityStack.pop();
            fakeActivityContainer.removeAllViews();
            if (!fakeActivityStack.isEmpty()) {
                resumeFake(fakeActivityStack.peek());
            }
        }
        LogUtil.i("pop: fake stack size = " + fakeActivityStack.size());
    }

    private void resumeFake(FakeActivity fake) {
        if (fake != null) {
            currentFakeActivity = fake;
            fakeActivityContainer.addView(currentFakeActivity.fakeActivityView);
            fake.onRestart();
            fake.onStart();
            fake.onResume();
        } else {
            fakeActivityStack.pop();
            //TODO
            Log.wtf(TAG, "another pop!");
        }
    }

    private void initUnity() {

        mUnityPlayer = new UnityPlayer(this);
        unityContainer.addView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFakeActivity != null) {
            currentFakeActivity.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }


    @Override
    public void onBackPressed() {
        if (mainLoading.getVisibility() == View.VISIBLE) {
            return;
        }
        if (fakeActivityStack.size() > 1) {
            currentFakeActivity.onBackPressed();
        } else {
            finish();
        }
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        if (currentFakeActivity != null) {
            currentFakeActivity.onDestroy();
        }
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        //mUnityPlayer.pause();
        if (currentFakeActivity != null) {
            currentFakeActivity.onPause();
        }
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
        if (currentFakeActivity != null) {
            currentFakeActivity.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentFakeActivity != null) {
            currentFakeActivity.onStart();
        }
        mUnityPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentFakeActivity != null) {
            currentFakeActivity.onStop();
        }
        //mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (currentFakeActivity != null) {
            currentFakeActivity.onLowMemory();
        }
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        return mUnityPlayer.injectEvent(event);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return mUnityPlayer.injectEvent(event);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /**
     * Unity runtime message
     *
     * @param message 参数以#分割
     */
    public void onUnityMessage(String message) {
        Log.i("Unity message:", message);
        final String[] args = message.split("#@#@");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (args[0].equals(Constants.UNITY_MESSAGE_SCENE_LOADING)) {
                    setLoadingViewVisible(View.VISIBLE);
                } else if (args[0].equals(Constants.UNITY_MESSAGE_SCENE_LOADED)) {
                    setLoadingViewVisible(View.INVISIBLE);
                }
                currentFakeActivity.onUnityMessage(args);
            }
        });
    }

    public void setLoadingViewVisible(int visible) {
        mainLoading.setVisibility(visible);
    }
}
