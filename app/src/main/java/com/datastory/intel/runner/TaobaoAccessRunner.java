package com.datastory.intel.runner;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.datastory.intel.utils.Accessibilitys;

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

                Accessibilitys.dfs(recursiveNode, ">", 0);

                System.out.println("该node是: " + child.getClassName() + ", " + child.getChildCount());
                for (int j = 0; j < child.getChildCount(); j++) {
                    Log.d(TAG, String.valueOf(child.getChild(j).getClassName() + ":" + child.getChild(j).getText()));
                }

                System.out.println("找到的child是:");
                List<AccessibilityNodeInfo> xy = child.findAccessibilityNodeInfosByText("泥");
                for (AccessibilityNodeInfo xyy : xy) {
                    System.out.println(xyy.getClassName() + ":" + xyy.getText());

                    Accessibilitys.trace_backward(xyy);
                }
            }
        }

        Log.d(TAG, "done <- " + PKG);
    }
}
