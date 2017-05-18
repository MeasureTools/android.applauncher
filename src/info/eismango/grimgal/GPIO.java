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
import android.os.IGPIOService;
import android.os.RemoteException;

import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.List;

public class GPIO {

	public final int  number;
	
	private static List<GPIO> GPIOS;
	private static IGPIOService GPIO_SERVICE;
	
	synchronized public static GPIO create(int number) {
		if (GPIO.GPIOS == null) {
			GPIO.GPIOS = new ArrayList<GPIO>();
		}
		if (GPIO.GPIO_SERVICE == null) {
			GPIO.GPIO_SERVICE = IGPIOService.Stub.asInterface(ServiceManager.getService("GPIO_SERVICE"));
		}
		for (GPIO g : GPIO.GPIOS) {
			if (g.number == number) {
				return g;
			}
		}
		try {
			GPIO.GPIO_SERVICE.export(number);
			GPIO.GPIO_SERVICE.setDirectionIn(number);
		}
		catch (Exception e) {
			Log.wtf("GPIO["+number+"](create)", e.toString());
			throw new RuntimeException(e);
		}
		GPIO g = new GPIO(number);
		GPIO.GPIOS.add(g);
		return g;
	}
	
	protected GPIO(int number) {
		info.eismango.grimgal.Log.v("GPIO["+this.number+"]", "INIT");
		this.number = number;
	}

	public boolean isIn() {
		try {
			return GPIO.GPIO_SERVICE.getDirection(this.number).equals("in");
		}
		catch (Exception e) {
			Log.wtf("GPIO["+this.number+"](isIn)", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	public boolean isOut() {
		try {
			return GPIO.GPIO_SERVICE.getDirection(this.number).equals("out");
		}
		catch (Exception e) {
			Log.wtf("GPIO["+this.number+"](isOut)", e.toString());
			throw new RuntimeException(e);
		}
	}

	public void setIn() {
		try {
			GPIO.GPIO_SERVICE.setDirectionIn(this.number);
		}
		catch (RemoteException e) {
			Log.wtf("GPIO["+this.number+"](setIn)", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	public void setOut() {
		try {
			GPIO.GPIO_SERVICE.setDirectionOut(this.number);
		}
		catch (RemoteException e) {
			Log.wtf("GPIO["+this.number+"](setOut)", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	public boolean getValue() {
		try {
			boolean value = GPIO.GPIO_SERVICE.getValue(this.number);
			info.eismango.grimgal.Log.v("GPIO["+this.number+"]", "GET: " + value);
			return value;
		}
		catch (RemoteException e) {
			Log.wtf("GPIO["+this.number+"](getValue)", e.toString());
			throw new RuntimeException(e);
		}
	}
	
	public void setValue(boolean value) {
		try {
			info.eismango.grimgal.Log.v("GPIO["+this.number+"]", "SET: " + value);
			GPIO.GPIO_SERVICE.setValue(this.number, value);
		}
		catch (RemoteException e) {
			Log.wtf("GPIO["+this.number+"](setValue)", e.toString());
			throw new RuntimeException(e);
		}
	}
}
