package com.dangerousthings.nfc;

import android.util.Log;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import org.apache.commons.codec.binary.Hex;

public class XposedMod implements IXposedHookLoadPackage {

  @Override
  public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
    if (!lpparam.packageName.equals("com.android.nfc")) return;

    XposedHelpers.findAndHookMethod("com.android.nfc.NfcService$TagService", lpparam.classLoader, "transceive", int.class, byte[].class, boolean.class, new XC_MethodHook() {
      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        byte[] command = (byte[]) param.args[1];
        Log.i("DangerousNFC", String.format("-> %s", new String(Hex.encodeHex(command)).toUpperCase()));

        byte[] response = (byte[]) XposedHelpers.callMethod(param.getResult(), "getResponseOrThrow"); // android.nfc.TransceiveResult
        Log.i("DangerousNFC", String.format("<- %s", new String(Hex.encodeHex(response)).toUpperCase()));
      }
    });
  }

}
