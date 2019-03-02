package com.datastory.intel.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Accessibilitys
 *
 * @author tobin
 * @since 2019-03-02
 */
public class Accessibilitys {

    private static final String TAG = Accessibilitys.class.getSimpleName();

    /**
     * 深度优先显示
     * 不准确, 但供参考
     *
     * @param root
     * @param indent
     * @param level
     */
    public static void dfs(AccessibilityNodeInfo root, String indent, int level) {
        // Attention:
        // 1. node下面不仅仅有child, 还有description
        // 2. getChildCount并不准确, 还需要getContentDescription
        // 3. 最佳方式应该是从uiautomatorviewer中dump出hierarchy view
        for (int i = 0; i < root.getChildCount(); i++) {
            AccessibilityNodeInfo child = root.getChild(i);
            Log.d(TAG, String.format("%s%s(%s) %s[%s]", indent, level, i, child.getClassName(), child.getText()));
            dfs(child, indent + "-", level + 1);
        }
    }

    /**
     * 安全地获取子node
     *
     * @param node
     * @param index
     * @return
     */
    public static AccessibilityNodeInfo getChildSafe(AccessibilityNodeInfo node, int index) {
        try {
            return node.getChild(index);
        } catch (Exception e) {
            Log.e(TAG, "get child error: " + node.getClassName() + ", " + index, e);
        }
        return null;
    }

    /**
     * 从找到的子节点向上回溯
     *
     * @param child
     */
    public static void trace_backward(AccessibilityNodeInfo child) {
        AccessibilityNodeInfo parent = child.getParent();
        String indent = "<";
        int index = 0;
        while (parent != null) {
            Log.d(TAG, String.format("%s%s %s[%s]", indent, index, parent.getClassName(), parent.getText()));

            parent = parent.getParent();
            indent += "-";
            index++;
        }
    }
}
