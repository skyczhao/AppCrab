package com.thirteenyu.crab.runner;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * TaobaoAccessRunner
 *
 * @author tobin
 * @since 2019-03-03
 */
public class TaobaoAccessRunner {
    public static final String PKG = "com.taobao.taobao";

    private static final String TAG = TaobaoAccessRunner.class.getSimpleName();

    // 前置过滤掉多余的父节点
    private int[] indexChain = new int[]{2, 0, 1, 0};

    public TaobaoAccessRunner() {

    }

    public void run(AccessibilityService context) {
        Log.d(TAG, "in -> " + PKG);

        AccessibilityNodeInfo recursiveNode = context.getRootInActiveWindow();
        // 收窄范围
        for (int index : indexChain) {
            if (recursiveNode.getChildCount() > index) {
                recursiveNode = recursiveNode.getChild(index);
            } else {
                break;
            }
        }

        // 获取内容
        if (recursiveNode.getClassName().equals("android.support.v7.widget.RecyclerView")) {
            // 遍历每个子节点获取
            for (int i = 0; i < recursiveNode.getChildCount(); i++) {
                AccessibilityNodeInfo child = recursiveNode.getChild(i);
                if (child.getChildCount() != 1) continue;
                child = child.getChild(0);

                try {
                    String name = String.valueOf(child.getChild(0).getContentDescription());
                    String price = child.getChild(2).getText() + "";
                    CharSequence decimal = child.getChild(3).getText();
                    if (decimal != null) {
                        price += decimal;
                    }
                    String nums = "";
                    List<AccessibilityNodeInfo> payCounts = child.findAccessibilityNodeInfosByText("付款");
                    if (payCounts.size() == 1) {
                        nums = String.valueOf(payCounts.get(0).getText());
                    }

                    System.out.println(name);
                    System.out.println(price);
                    System.out.println(nums);
                } catch (Exception e) {
                    // skip
//                    Log.e(TAG, "parse error", e);
                }
            }
        }

        Log.d(TAG, "done <- " + PKG);
    }
}
