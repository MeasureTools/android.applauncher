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

import java.io.IOException;

//TODO: Implement all missing android.util.Log methods!
public class Log {

	private static StringLogWriter logWriter = null;

	synchronized static void init (String directory, String name) throws IOException {
		if (Log.logWriter != null) {
			throw new RuntimeException("Log can only be initialized once!");
		}
		Log.logWriter = new StringLogWriter(directory, name + "_log");
	}


	public static int v(String tag, String msg) {
		int r = android.util.Log.v(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:VERBOSE][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}

	public static int d(String tag, String msg) {
		int r = android.util.Log.d(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:DEBUG][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}

	public static int i(String tag, String msg) {
		int r = android.util.Log.i(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:INFO][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}

	public static int w(String tag, String msg) {
		int r = android.util.Log.w(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:WARN][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}

	public static int e(String tag, String msg) {
		int r = android.util.Log.e(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:ERROR][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}

	public static int wtf(String tag, String msg) {
		int r = android.util.Log.wtf(tag, msg);
		if (Log.logWriter == null) {
			return r;
		}
		try {
			Log.logWriter.write("[Tag:"+tag+"][Level:ASSERT][Msg:"+msg+"]");
		}
		catch (IOException e) {
			try {
				Log.logWriter.event(e.toString());
			} catch (IOException e1) {
				android.util.Log.wtf(tag, e1);
			}
		}
		return r;
	}
}
