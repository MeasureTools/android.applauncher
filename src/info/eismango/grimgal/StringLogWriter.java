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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class StringLogWriter {

	private final Writer data;
	private final Writer meta;
	private final Writer event;

	private final long   init;

	public StringLogWriter (String directory, String name) throws IOException {
		(new File(directory)).mkdirs();
		
		this.init = System.currentTimeMillis();
		this.data = new OutputStreamWriter(new FileOutputStream(directory+"/"+name+".data"), "UTF-8");
		this.meta = new OutputStreamWriter(new FileOutputStream(directory+"/"+name+".meta"), "UTF-8");
		{
			this.meta.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			this.meta.write("<grim version=\"1.0\">\n");
			this.meta.write("	<meta>\n");
			this.meta.write("		<name>"+name+"</name>\n");
			this.meta.write("		<format>time|:|VALUE</format>\n");
			this.meta.write("		<init>"+this.init+"</init>\n");
			this.meta.write("	</meta>\n");
			this.meta.write("	<values>\n");
			this.meta.write("		<value name=\"time\" type=\"string\" unit=\"ms\"/>\n");
			this.meta.write("		<value name=\"VALUE\" type=\"string\" unit=\"\" />\n");
			this.meta.write("	</values>\n");
			this.meta.write("</grim>\n");
		}
		this.meta.flush();
		this.event = new OutputStreamWriter(new FileOutputStream(directory+"/"+name+".event"), "UTF-8");
	}

	public void write (String msg) throws IOException {
		this.data.write((System.currentTimeMillis() - this.init) + ":" + msg.replace("\n", " _n ").replace("\r", "") + "\n");
		this.data.flush();
	}

	public void event (String msg) throws IOException {
		this.event.write((System.currentTimeMillis() - this.init) + ":" + msg.replace("\n", " _n ").replace("\r", "") + "\n");
		this.event.flush();
	}
}
