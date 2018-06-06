package cn.wycode.fakeactivityforunity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import cn.wycode.fakeactivityforunity.utils.LogUtil;


/**
 * Fake Activity for unity.
 * The lifecycle method is controlled by UnityActivity.
 * UnityActivity is a true Activity
 * Created by wayne on 2017/12/19.
 */

public abstract class FakeActivity {


    public View fakeActivityView;

    protected UnityActivity mContext;

    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(this.getClass(), "onCreate");
    }

    protected void onRestart() {
        LogUtil.i(this.getClass(), "onRestart");
    }

    protected void onStart() {
        LogUtil.i(this.getClass(), "onStart");
    }

    protected void onResume() {
        LogUtil.i(this.getClass(), "onResume");
    }


    protected void onStop() {
        LogUtil.i(this.getClass(), "onStop");
    }

    protected void onPause() {
        LogUtil.i(this.getClass(), "onPause");
    }

    protected void onDestroy() {
        LogUtil.i(this.getClass(), "onDestroy");
        mContext.popFake();
    }

    public void onLowMemory() {

    }

    protected void onSaveInstanceState(Bundle outState) {

    }

    protected void onBackPressed() {
        finish();
    }

    protected void finish() {
        this.onPause();
        this.onStop();
        this.onDestroy();
    }

    public void runOnUiThread(Runnable runnable) {
        mContext.runOnUiThread(runnable);
    }

    protected <V extends View> V findViewById(@IdRes int id) {
        return mContext.findViewById(id);
    }

    protected void setContentView(@LayoutRes int layoutId) {
        fakeActivityView = LayoutInflater.from(mContext).inflate(layoutId, null);
        mContext.fakeViewInflated(fakeActivityView);
    }

    protected void startActivity(Class<? extends FakeActivity> activityClass) {
        startActivity(activityClass, null);
    }

    protected void startActivity(Class<? extends FakeActivity> activityClass, Bundle data) {
        this.onPause();
        this.onStop();
        mContext.startActivity(activityClass, data);
    }


    /**
     * 仅用于跳出APP
     * 打开新UI页，使用startActivity(FakeActivity.class)方法
     *
     * @param i intent
     */
    protected void startActivity(Intent i) {
        mContext.startActivity(i);
    }

    protected void setOnClickListeners(View.OnClickListener onClickListener, View... views) {
        for (View e : views) {
            e.setOnClickListener(onClickListener);
        }
    }

    /**
     * Unity runtime message
     *
     * @param args 参数
     */
    protected void onUnityMessage(String[] args) {
    }


    protected void showLoading() {
        mContext.setLoadingViewVisible(View.VISIBLE);
    }

    protected void hideLoading() {
        mContext.setLoadingViewVisible(View.INVISIBLE);
    }
}
