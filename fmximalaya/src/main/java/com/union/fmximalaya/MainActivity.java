package com.union.fmximalaya;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import java.io.File;
import com.union.commonlib.ui.view.TintUtils;
import com.union.fmximalaya.plugin.IPlugin;
import com.union.fmximalaya.ui.activity.ListTestActivity;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import dalvik.system.DexClassLoader;


public class MainActivity extends AppCompatActivity {
    AppCompatTextView textView;
    ImageView imageView;
    Activity atc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.atc = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        imageView = (ImageView)findViewById(R.id.example_img);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                AnimShow.showAnim(atc, imageView);
//                //getWebViewPackageInfo();
//                dynamicLoadDex();

                startActivity(new Intent(MainActivity.this, ListTestActivity.class));
            }
        });

        textView = (AppCompatTextView) findViewById(R.id.image);
        TintUtils.setBackgroundTint(this, textView, R.color.yellow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void dynamicLoadDex() {
        File dexFile = new File(Environment.getExternalStorageDirectory().toString()
                + File.separator + "plugin_dex.jar");

        // optimized directory, the applciation and package directory
        final File optimizedDexOutputPath = getDir("outdex", 0);

        // DexClassLoader to get the file and write it to the optimised directory
        DexClassLoader classLoader = new DexClassLoader(dexFile.getAbsolutePath(),
                optimizedDexOutputPath.getAbsolutePath(),null, getClassLoader());
        try {
            Class libProviderClazz = classLoader.loadClass("com.union.fmximalaya.plugin.PluginTest");
            IPlugin plugin = (IPlugin)libProviderClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ;
    }

    private void getWebViewPackageInfo() {
        PackageManager pm = this.getPackageManager();
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod(
                    "addAssetPath", String.class);

            Context webViewContext = this.createPackageContext("com.union.fmdouban", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            String dexPath = webViewContext.getApplicationInfo().sourceDir;
            addAssetPath.invoke(assetManager, dexPath);
            ClassLoader clazzLoader = webViewContext.getClassLoader();
//            StrictMode.allowThreadDiskReads();
//            PackageInfo info = pm.getPackageInfo("com.union.fmdouban", PackageManager.GET_META_DATA);
//            ApplicationInfo applicationInfo = info.applicationInfo;
//            Log.i("veve", "=========  " + applicationInfo.metaData.getString("com.android.webview.WebViewLibrary"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
