/**
 * Copyright 2016 Daniel "Dadie" Korner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * Unless required by applicable law or agreed to in writing, software
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.grimgal.android.applauncher;

import android.net.Uri;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.IValueSupplierManager;

import com.grimgal.android.framework.*;

public class AppLauncherActivity extends Activity {

	private String storagePath;

	List<Button> buttons = new ArrayList<Button>();
	ScrollView   scroll;
	LinearLayout layout;
	LayoutParams layoutParams;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		int[] arr = {33, 23, 29, 30, 22, 18, 21, 190, 191, 192, 174, 173, 171, 172, 189, 210, 209, 19, 28, 31, 25, 24};
		for (int i : arr) {
			GPIO g = GPIO.create(i);
			{
				g.setOut();
				g.setValue(false);
			}
		}

		// ###### Layout [BEGIN] ######

		this.layout = new LinearLayout(this);
		{
			this.layout.setOrientation(LinearLayout.VERTICAL);
		}
		this.scroll = new ScrollView(this);
		{
			this.scroll.addView(this.layout);
		}
		this.setContentView(this.scroll);
		//setContentView(R.layout.dynamicview);
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		{
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		}
		PackageManager pm = this.getPackageManager();
		List<ResolveInfo> resInfoList = pm.queryIntentActivities(mainIntent, 0);
		for (final ResolveInfo resInfo : resInfoList) {
			final String appName = (resInfo.loadLabel(pm)).toString();

			Button button = new Button(this);
			{
				button.setHeight(20);
				button.setWidth(200);
				//button.setTag(this.buttons.size());
				button.setText(appName);

				OnClickListener buttonClicked = new OnClickListener() {
					@Override
					public void onClick(View v) {
						String prefix = appName + "_" + System.currentTimeMillis();
						//Start all Logger
						try {
							com.grimgal.android.framework.Framework.init(storagePath, prefix);
							IValueSupplierManager ivsm = IValueSupplierManager.Stub.asInterface(ServiceManager.getService("VALUE_SUPPLIER_MANAGER"));
							if (ivsm == null) {
								com.grimgal.android.framework.Log.e("supplier","ivsm was null");
							}
							String[] services = ivsm.list();
							com.grimgal.android.framework.Log.v("AppLauncher","Found " +services.length+" Suppliers");
							for(int k =0 ; k < services.length ; k++) {
								com.grimgal.android.framework.Log.v("supplier",services[k]);
							}
							for(int k =0 ; k < services.length ; k++) {
								com.grimgal.android.framework.ValueLog.run(services[k], 100, java.util.concurrent.TimeUnit.MILLISECONDS);
							}
						}
						catch (IOException e) {
							com.grimgal.android.framework.Log.e("AppLauncher",e.toString());
							//...
						}
						catch (RemoteException e2) {
							com.grimgal.android.framework.Log.e("AppLauncher",e2.toString());
						}
						catch (Exception e3) {
							com.grimgal.android.framework.Log.e("AppLauncher",e3.toString());
						}

						ActivityInfo activity = resInfo.activityInfo;
						ComponentName componentName = new ComponentName(activity.applicationInfo.packageName, activity.name);
						Intent appIntent = new Intent(Intent.ACTION_MAIN);
						{
							appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							appIntent.setComponent(componentName);
						}
						GPIO g210 = GPIO.create(210);
						g210.setOut();
						g210.setValue(true);
						GPIO g19 = GPIO.create(19);
						g19.setOut();
						g19.setValue(true);

						startActivity(appIntent);
					}
				};
 				button.setOnClickListener(buttonClicked);
			}
			this.layout.addView(button);
			this.buttons.add(button);
		}
		// ###### Layout [END] ######

		this.storagePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	}

}
