/************************************************************************************
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 * 
 **************************************************************************************/
package com.itsaky.layoutinflater.util;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import static com.itsaky.androidide.ui.util.Preconditions.*;

import androidx.annotation.NonNull;

/**
 * Reads and manages values (colors, strings, integers, etc.)
 *
 * @author Akash Yadav
 */
public class ValuesTable {

    private final Map<String, String> strings = new HashMap<>();
    private final Map<String, String> colors = new HashMap<>();
    private final Map<String, String> dimens = new HashMap<>();

    private boolean initialized = false;
    
    private static final Logger LOG = Logger.instance("ValuesTable");

    /**
     * Create a new table from the values directories.
     * Every file in the provided directories will be read
     * and this table will be filled with that data.
     *
     * @param dirs The 'values' directories
     */
    public ValuesTable(File... dirs) {
        assertNotnull(dirs, "Values directory cannot be null!");
        final CompletableFuture<Void> read = CompletableFuture.runAsync(
                new ReadData (
                        dirs,
                        strings,
                        colors,
                        dimens
                )
        );
        
        read.whenComplete((__, th) -> {
            if (th != null) {
                LOG.error(BaseApplication.getBaseInstance().getString(com.itsaky.layoutinflater.R.string.err_cannot_read_values), th);
            } else {
                initialized = true;
            }
        });
    }
    
    public boolean isInitialized () {
        return initialized;
    }

    @Override
    public int hashCode() {
        return Objects.hash(strings, colors, dimens);
    }
    
    /**
     * Used to read values data async.
     */
    static class ReadData implements Runnable {
        
        private final File[] dirs;
        private final Map <String, String> strings, colors, dimens;

        public ReadData(File[] dirs, Map<String, String> strings, Map<String, String> colors, Map<String, String> dimens) {
            this.dirs = dirs;
            this.strings = strings;
            this.colors = colors;
            this.dimens = dimens;
        }
        
        @Override
        public void run() {
            for (File dir : dirs) {
                readFrom(dir);
            }
        }
        
        /**
         * Lists all the file from the 'values' directories and
         * and reads the data.
         *
         * @param dir The 'values' directory.
         */
        private void readFrom(@NonNull File dir) {
            final var files = dir.listFiles (this::isXml);
            if (files != null) {
                for (File file : files) {
                    readValues(file);
                }
            }
        }

        /**
         * Reads data from the provided file.
         *
         * @param file The file to read
         */
        private void readValues(File file) {
            if (!FileUtils.isUtf8(file)) {
                return;
            }
            readValues(FileIOUtils.readFile2String(file));
        }

        /**
         * Reads the data from the provided XML content. The should
         * be read from a values file (res/values/<file>.xml).
         *
         * @param content The read content.
         */
        private void readValues(String content) {
            final Document doc = Jsoup.parse(content, "", Parser.xmlParser());
            final Elements els = doc.getElementsByTag("resources");

            if (els.size() <= 0) {
                return;
            }

            final Element res = els.first();
            if (res == null) {
                return;
            }

            for (Element child : res.children()) {
                final String tag = child.tagName().trim();

                switch (tag) {
                    case "string" :
                        readString(child);
                        break;
                    case "color" :
                        readColor(child);
                        break;
                }
            }

            // TODO Resolve references to other values
        }

        /**
         * Read the color data from the element.
         *
         * @param element The XML element containing data about the color.
         */
        private void readColor(Element element) {
            genericRead(element, strings);
        }

        /**
         * Read the string data from the element.
         *
         * @param element The XML element containing data about the string.
         */
        private void readString(Element element) {
            genericRead(element, colors);
        }

        /**
         * Common method for reading an XML element which has the 'name' attribute
         * containing the key of the entry and its {@code text()} returns the value
         * of the entry. If reading the element is successful, an entry will be
         * added to the {@code putTo} map.
         *
         * @param element The element to get data from.
         * @param putTo The map to which the read data will be added (or updated).
         */
        private void genericRead(@NonNull Element element, Map<String, String> putTo) {
            final String name = element.attr("name");
            if (name.trim().length() <= 0) {
                return;
            }
            
            final String value = element.text();

            putTo.put(name, value);
        }
        
        private boolean isXml (@NonNull File file) {
            return file.isFile() && file.getName().endsWith(".xml");
        }
    }
}
