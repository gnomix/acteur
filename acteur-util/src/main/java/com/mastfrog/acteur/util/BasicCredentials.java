/* 
 * The MIT License
 *
 * Copyright 2013 Tim Boudreau.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mastfrog.acteur.util;

import io.netty.util.CharsetUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Tim Boudreau
 */
public class BasicCredentials {
    public final String username;
    public final String password;

    public BasicCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    private static final Pattern HEADER = Pattern.compile("Basic (.*)");
    private static final Pattern UNPW = Pattern.compile("(.*?):(.*)");

    public static BasicCredentials parse(String header) {
        Matcher m = HEADER.matcher(header);
        if (m.matches()) {
            String base64 = m.group(1);
            byte[] bytes = base64.getBytes(CharsetUtil.US_ASCII);
            if (Base64.isArrayByteBase64(bytes)) {
                bytes = Base64.decodeBase64(bytes);
            }
            String s = new String(bytes, CharsetUtil.UTF_8);
            m = UNPW.matcher(s);
            if (m.matches()) {
                String username = m.group(1);
                String password = m.group(2);
                return new BasicCredentials(username, password);
            }
        }
        return null;
    }

    public String toString() {
        String merged = username + ':' + password;
        byte[] b = merged.getBytes(CharsetUtil.UTF_8);
        b = Base64.encodeBase64(b);
        return "Basic " + new String(b, CharsetUtil.US_ASCII);
    }

}
