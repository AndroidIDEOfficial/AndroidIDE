/*
 * Copyright (C) 2017, Megatron King
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.megatronking.stringfog.xor;

import com.github.megatronking.stringfog.Base64;
import com.github.megatronking.stringfog.IStringFog;

import java.io.UnsupportedEncodingException;

/**
 * StringFog base64+xor encrypt and decrypt implementation.
 *
 * @author Megatron King
 * @since 2018/9/2 14:34
 */
public final class StringFogImpl implements IStringFog {

    private static final String CHARSET_NAME_UTF_8 = "UTF-8";

    @Override
    public String encrypt(String data, String key) {
        String newData;
        try {
            newData = new String(Base64.encode(xor(data.getBytes(CHARSET_NAME_UTF_8), key), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            newData = new String(Base64.encode(xor(data.getBytes(), key), Base64.NO_WRAP));
        }
        return newData;
    }

    @Override
    public String decrypt(String data, String key) {
        String newData;
        try {
            newData = new String(xor(Base64.decode(data, Base64.NO_WRAP), key), CHARSET_NAME_UTF_8);
        } catch (UnsupportedEncodingException e) {
            newData = new String(xor(Base64.decode(data, Base64.NO_WRAP), key));
        }
        return newData;
    }

    @Override
    public boolean overflow(String data, String key) {
        return data != null && data.length() * 4 / 3 >= 65535;
    }

    private static byte[] xor(byte[] data, String key) {
        int len = data.length;
        int lenKey = key.length();
        int i = 0;
        int j = 0;
        while (i < len) {
            if (j >= lenKey) {
                j = 0;
            }
            data[i] = (byte) (data[i] ^ key.charAt(j));
            i++;
            j++;
        }
        return data;
    }

}
