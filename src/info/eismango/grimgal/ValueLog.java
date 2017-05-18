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

package info.eismango.grimgal;

import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.IValueSupplierManager;
import android.os.RemoteException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class ValueLog {

	private static ScheduledExecutorService scheduler;
	private static Map<String, ScheduledFuture<?>> supplierFutures;
	private static String directory;
	private static String name;

	static IValueSupplierManager valueSupplierManager;

	synchronized static void init (String directory, String name) {
		if (ValueLog.scheduler != null) {
			throw new RuntimeException("ValueLog can only be initialized once!");
		}
		ValueLog.scheduler       = Executors.newScheduledThreadPool(16);
		ValueLog.supplierFutures = new HashMap<String, ScheduledFuture<?>>();
		ValueLog.directory       = directory;
		ValueLog.name            = name;
		ValueLog.valueSupplierManager = IValueSupplierManager.Stub.asInterface(ServiceManager.getService("VALUE_SUPPLIER_MANAGER"));
	}

	synchronized public static void run(String supplier, long period, TimeUnit unit) throws IOException, RemoteException {
		if (ValueLog.scheduler == null) {
			throw new RuntimeException("ValueLog need to be initialized!");
		}
		if (ValueLog.supplierFutures.containsKey(supplier)) {
			throw new RuntimeException("Each Supplier can only run once!");
		}
		if (ValueLog.valueSupplierManager.size(supplier) == -1) {
			throw new RuntimeException("Unknown value supplier " + supplier);
		}
		ValueLogger valueLogger   = new ValueLogger(supplier, ValueLog.directory, ValueLog.name);
		ScheduledFuture<?> future = ValueLog.scheduler.scheduleAtFixedRate(valueLogger, 0, period, unit);
		ValueLog.supplierFutures.put(supplier, future);
	}
}
