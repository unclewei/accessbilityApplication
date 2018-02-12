package com.example.unclewei.accessbilityapplication.service;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.unclewei.accessbilityapplication.alarm.Status;
import com.example.unclewei.accessbilityapplication.alarm.UploadAlarmUtil;

import java.util.List;

/**
 * @author unclewei
 * @Data 2018/2/8.
 */

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private static final String HOME_CLASSNAME = "com.tencent.mm.ui.LauncherUI";//主页面的className

    private static final String CHAT_BACK_ID = "com.tencent.mm:id/h4";//聊天按钮返回按钮的id
    private static final String SNS_BACK_ID = "com.tencent.mm:id/hj";//朋友圈返回按钮的id
    private static final String UNREAD_RED_POINT = "com.tencent.mm:id/aoi";//未读数的id
    private static final String CHAT_MESSAGE_COUNT = "com.tencent.mm:id/a_d";//列表消息超过20条的提示栏的id
    private static final String CHAT_LIST_VIEW = "com.tencent.mm:id/bya";//聊天列表listView的id
    private static final String SNS_LIST_VIEW = "com.tencent.mm:id/d5m";//朋友圈列表listView的id
    private static final String BOTTOM_WE_CHAT = "com.tencent.mm:id/c3c";//底部微信的按钮id
    private static final String CIRCLE_OF_FRIENDS = "com.tencent.mm:id/a94";//朋友圈id

    private int scrollTime = 0;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.e("william", "发生event事件"+eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (HOME_CLASSNAME.equals(event.getClassName().toString())) {
                    switch (Status.CLICK_TEXT) {
                        case Status.friendsGroup:
                            //获取群消息
                            weChatGroup();
                            break;
                        case Status.scanGrouperData:
                            //获取朋友圈消息
                            scanSNS();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.e("william", "发生了点击事件");
                AccessibilityNodeInfo click = getRootInActiveWindow().getChild(0);
                if (click != null) {
                    Log.e("william click", click.toString());
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.e("william", "发生了  滚动  事件");
                break;
            default:
                break;
        }
    }


    /*
     * --------------------------------------------------------------------------------------------------------------
     */

    /**
     * 浏览朋友圈
     */
    private void scanSNS() {
        try {
            back();
            sleep(1000);
            clickNodesByID(CIRCLE_OF_FRIENDS);
            sleep(500);
            scrollTime = 0;
            while (scrollTime < 50) {
                sleep(1000);
                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                if (nodeInfo == null) {
                    Log.e("william", "找不到  getRootInActiveWindow()");
                    return;
                }
                List<AccessibilityNodeInfo> chatList = nodeInfo.findAccessibilityNodeInfosByViewId(SNS_LIST_VIEW);
                if (chatList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "找不到list", Toast.LENGTH_SHORT).show();
                    return;
                }
                chatList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                sleep(1000);
                scrollTime++;
            }
            sleep(1000);
            Status.CLICK_TEXT = "xxx";
            clickNodesByID(SNS_BACK_ID);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Status.CLICK_TEXT = "xxx";
        }


    }




    /*
     * --------------------------------------------------------------------------------------------------------------
     */

    /**
     * 微信群 功能
     */
    private void weChatGroup() {
        try {
            back();
            sleep(1000);
            doubleClickNodesByID(BOTTOM_WE_CHAT);
            sleep(1500);
            scrollTime = 0;
            scanWeChatGroup();
            sleep(1000);
            Status.CLICK_TEXT = "xxx";
        } catch (InterruptedException e) {
            e.printStackTrace();
            Status.CLICK_TEXT = "xxx";
        }
    }

    /**
     * 浏览微信群
     */
    private void scanWeChatGroup() throws InterruptedException {
        while (getCountNodesByID(UNREAD_RED_POINT) > 0) {
            //点击进入群中
            clickNodesByID(UNREAD_RED_POINT);
            sleep(3000);
            //查看是否有查看更多
            seeMoreMessage();
            sleep(1000);
        }
        //滚动向下然后再遍历
        if (scrollTime < 3) {
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo == null) {
                return;
            }
            List<AccessibilityNodeInfo> chatList = nodeInfo.findAccessibilityNodeInfosByViewId(CHAT_LIST_VIEW);
            if (chatList.size() == 0) {
                Toast.makeText(getApplicationContext(), "找不到list", Toast.LENGTH_SHORT).show();
                return;
            }
            chatList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            scrollTime++;
            scanWeChatGroup();
        }
    }


    /**
     * 根据id寻找节点并 点击
     */
    private void clickNodesByID(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.e("william", "找不到  getRootInActiveWindow()");
            return;
        }
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodes != null && !nodes.isEmpty()) {
            click(nodes.get(0));
        } else {
            Log.e("william", id + "遍历结果为空");
        }
    }

    /**
     * 根据id寻找节点并 双击
     */
    private void doubleClickNodesByID(String id) throws InterruptedException {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodes != null && !nodes.isEmpty()) {
            click(nodes.get(0));
            sleep(500);
            click(nodes.get(0));
        } else {
            Log.e("william", id + "遍历结果为空");
        }
    }

    /**
     * 获取更多信息，有的话就向上滑
     */
    private void seeMoreMessage() throws InterruptedException {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        Log.e("william","找到更多信息的数量"+getCountNodesByID(CHAT_MESSAGE_COUNT) );
        while (getCountNodesByID(CHAT_MESSAGE_COUNT) > 0) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            sleep(1000);
        }
        clickNodesByID(CHAT_BACK_ID);

    }

    /**
     * 根据id寻找节点数量
     */
    private int getCountNodesByID(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return 0;
        }
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        return nodes.size();
    }

    private void click(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        } else if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(nodeInfo.getParent());
        }

    }

    private void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    private void back() throws InterruptedException {
        while (true) {
            int backCount = getCountNodesByID(CHAT_BACK_ID);
            if (backCount == 0) {
                return;
            }
            clickNodesByID(CHAT_BACK_ID);
            sleep(1000);
        }
    }

    /**
     * 服务连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "服务开启", Toast.LENGTH_SHORT).show();
        UploadAlarmUtil.invokeRefreshAlarmManager(getApplicationContext());
        super.onServiceConnected();
    }

    @Override
    public void onInterrupt() {

    }
}
