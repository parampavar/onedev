/*
 * Copyright  2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.gitplex.server.util.execution;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.google.common.base.Charsets;

/**
 * heavily inspired from LogOutputStream
 * this stream class calls back the P4Handler on each line of stdout or stderr read
 * @author : <a href="mailto:levylambert@tiscali-dsl.de">Antoine Levy-Lambert</a>
 *
 * Modified by <a href="mailto:robin@pmease.com">robin shen</a> to seperate
 * output and error handling
 *
 */
public abstract class LineConsumer extends OutputStream {
	
	/**
	 * If this property is not empty, output will be re-directed to this stream
	 * instead of the ant log stream
	 */
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    private boolean skip = false;
    
    private String encoding;
    
    public LineConsumer(String encoding) {
    	this.encoding = encoding;
    }
    
    public LineConsumer() {
    	this(Charsets.UTF_8.name());
    }
    
    public String getEncoding() {
    	return encoding;
    }
    
    /**
     * Write the data to the buffer and flush the buffer, if a line
     * separator is detected.
     *
     * @param cc data to log (byte).
     * @throws IOException IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    @Override
	public void write(int cc) throws IOException {
		final byte c = (byte) cc;
        if ((c == '\n') || (c == '\r')) {
            if (!skip) {
                processBuffer();
            }
        } else {
            buffer.write(cc);
        }
        skip = (c == '\r');
    }

    /**
     * Converts the buffer to a string and sends it to <code>processLine</code>
     */
    protected void processBuffer() {
		try {
			consume(encoding!=null?buffer.toString(encoding):buffer.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} finally {
			buffer.reset();
		}
    }

	/**
	 * Sub class must implement this method to handle a line of output
	 * @param line
	 */
	public abstract void consume(String line);
	
	@Override
	public void flush() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
        super.flush();
	}

	/**
     * Writes all remaining
     * @throws IOException if an I/O error occurs.
     */
    @Override
	public void close() throws IOException {
    	flush();
    	super.close();
    }
    
}


