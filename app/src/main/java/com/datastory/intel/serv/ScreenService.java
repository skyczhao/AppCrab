package com.datastory.intel.serv;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * ScreenService
 *
 * @author tobin
 * @since 2017-10-18
 */
public class ScreenService extends AccessibilityService {
    private static final String TAG = ScreenService.class.getSimpleName();

    @Override
    public void onServiceConnected() {
        Toast.makeText(this, "屏幕采集开启", Toast.LENGTH_SHORT).show();
    }

    /**
     * 响应事件
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 只处理窗口变化事件
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            // 检测activity是否存在
            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;
            if (isActivity) {
                Toast.makeText(this,
                        event.getPackageName().toString(),
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, event.getPackageName().toString());
                Log.d(TAG, event.getClassName().toString());
            }

        }
    }

    /**
     * 检查对应组件是否能在系统中获取
     *
     * @param componentName
     * @return
     */
    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "屏幕采集关闭", Toast.LENGTH_SHORT).show();
    }
}

