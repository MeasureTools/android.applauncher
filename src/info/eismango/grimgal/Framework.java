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
import android.os.Environment;

import java.io.IOException;

public class Framework {

	private static boolean isInit = false;

	private static String directory;
	private static String name;

	synchronized public static void init(String directory, String name) throws IOException {
		if (Framework.isInit) {
			throw new RuntimeException("Framework already initialized!");
		}
		Framework.isInit    = true;
		Framework.directory = directory;
		Framework.name      = name;
		Log.init(directory, name);
		ValueLog.init(directory, name);
	}

}
