package com.base.app.utils;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static volatile AppManager sInstance;

    /**
     * 隐藏构造器
     */
    private AppManager() {
    }

    /**
     * 单例
     * @return 返回AppManager的单例
     */
    public static AppManager getInstance() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity activity实例
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.remove(activity);
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return 当前（栈顶）activity
     */
    public Activity currentActivity() {
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            return mActivityStack.lastElement();
        }
        return null;
    }

    /**
     * 结束除当前activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param activity 不需要结束的activity
     */
    public void finishOtherActivity(Activity activity) {
        if (mActivityStack != null) {
            for (Iterator<Activity> it = mActivityStack.iterator(); it.hasNext(); ) {
                if (it.hasNext()) {
                    Activity temp = it.next();
                    if (temp != null && temp != activity) {
                        temp.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束除这一类activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param cls 不需要结束的activity
     */
    public void finishOtherActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Iterator<Activity> it = mActivityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                if (!activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            Activity activity = mActivityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 指定的activity实例
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (mActivityStack != null) {// 兼容未使用AppManager管理的实例
                mActivityStack.remove(activity);
            }
            activity.finish();
        }
    }

    /**
     * 结束指定类名的所有Activity
     *
     * @param cls 指定的类的class
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Iterator<Activity> it = mActivityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (int i = mActivityStack.size() - 1; i >= 0; i--) {
                if (null != mActivityStack.get(i)) {
                    mActivityStack.get(i).finish();
                }
            }
            mActivityStack.clear();
        }
    }

    /**
     * activity是否存在
     */
    public Boolean hasActivity(Class<?> classN) {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                Logger.INSTANCE.e(activity.getClass().getName());
                if (activity.getClass().equals(classN)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取数组个数
     */
    public int getSize() {
        return mActivityStack.size();
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }
}