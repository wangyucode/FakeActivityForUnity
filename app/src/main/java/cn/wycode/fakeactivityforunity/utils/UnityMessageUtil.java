package cn.wycode.fakeactivityforunity.utils;

import com.unity3d.player.UnityPlayer;

/**
 * Created by wayne on 2017/12/20.
 */

public final class UnityMessageUtil {

    public static void sendMessageToUnity(String method, String... args){
        StringBuilder argsBuilder = new StringBuilder();
        for (String arg:args) {
            argsBuilder.append(arg);
        }

        LogUtil.i(UnityMessageUtil.class,argsBuilder.toString());
        
        UnityPlayer.UnitySendMessage("MessageCenter",method,argsBuilder.toString());
    }
}
