package top.dh.fy;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.dh.fy.presenter.HookApis;
import top.dh.fy.presenter.SdkHookManager;
import top.dh.fy.util.XposedUtil;

/**
 * Created by xuqingfu on 2017/4/15.
 */

public class XPosedPlugin implements IXposedHookLoadPackage {

    private static final String TAG = "silence";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (!XposedUtil.isNeedHook(loadPackageParam.packageName)) { //过滤程序
            return;
        }
        Log.i(TAG, "加载Hook程序：" + loadPackageParam.packageName);

        SdkHookManager.findMethodIsFromMockProvider();

        SdkHookManager.findMethodGetInt(loadPackageParam);

        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.v(TAG, "启动程序：" + param.thisObject.toString());
                ClassLoader appClassLoader = ((Context)param.args[0]).getClassLoader();
                try {
                    HookApis.findMethodAmapLongitudeAndLatitude(appClassLoader);

                }
                catch(Exception e) {
                    Log.e(TAG, "Hook发生异常：" + e.toString());
                }
            }
        });


    }


}
