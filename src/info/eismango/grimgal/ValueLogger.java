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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.IValueSupplierManager;
import android.os.RemoteException;

public class ValueLogger implements Runnable {

	private final ByteLogWriter logWriter;
	private final String supplier;

	public ValueLogger (String supplier, String directory, String name) throws IOException, RemoteException {
		String type    = ValueLog.valueSupplierManager.type(supplier);
		int    size    = ValueLog.valueSupplierManager.size(supplier);
		String unit    = ValueLog.valueSupplierManager.unit(supplier);
		this.logWriter = new ByteLogWriter(directory, name + "_" + supplier, size, type, unit);
		this.supplier  = supplier;
	}

	@Override
	public void run () {
		try {
			byte[] byteValue = ValueLog.valueSupplierManager.value(this.supplier);
			this.logWriter.write(byteValue);
		}
		catch (Exception e) {
			Log.wtf("ValueLogger:"+this.supplier, e.toString());
		}
	}
}
