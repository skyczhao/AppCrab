package com.datastory.intel.serv;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.datastory.intel.runner.TaobaoAccessRunner;

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
        // TYPE_WINDOW_CONTENT_CHANGED 内容变化也触发?
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            // 检测activity是否存在
            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;
            if (isActivity) {
                if (event.getPackageName().equals(TaobaoAccessRunner.PKG)) {
                    Toast.makeText(this, "淘宝APP活跃中...", Toast.LENGTH_SHORT).show();
                    new TaobaoAccessRunner().run(this);
                }
                // TODO: more app
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

