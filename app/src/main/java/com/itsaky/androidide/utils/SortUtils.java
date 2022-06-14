package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileUtils;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

  private SortUtils() {}

  private static final int SORT_ALPHA = 0;
  private static final int SORT_TYPE = 1;
  private static final int SORT_SIZE = 2;
  private static final int SORT_DATE = 3;

  public static void sortList(List<String> content, String current) {
    PreferenceManager manager = StudioApp.getInstance().getPrefManager();

    int len = content != null ? content.size() : 0;

    if (len == 0) return;

    int index = 0;
    String[] items = new String[len];
    content.toArray(items);

    switch (manager.getInt("pref_sort_file", 1)) {
      case SORT_ALPHA:
        Arrays.sort(items, Comparator_ALPH);
        content.clear();

        Collections.addAll(content, items);
        break;
      case SORT_SIZE:
        Arrays.sort(items, Comparator_SIZE);
        content.clear();

        for (String a : items) {
          if (new File(current + "/" + a).isDirectory()) content.add(index++, a);
          else content.add(a);
        }
        break;
      case SORT_TYPE:
        Arrays.sort(items, Comparator_TYPE);
        content.clear();

        for (String a : items) {
          if (new File(current + "/" + a).isDirectory()) content.add(index++, a);
          else content.add(a);
        }
        break;

      case SORT_DATE:
        Arrays.sort(items, Comparator_DATE);
        content.clear();

        for (String a : items) {
          if (new File(current + "/" + a).isDirectory()) content.add(index++, a);
          else content.add(a);
        }
        break;
    }

    if (manager.getBoolean("pref_reverse_list", false)) {
      Collections.reverse(content);
    }
  }

  private static final Comparator<? super String> Comparator_ALPH =
      new Comparator<String>() {

        @Override
        public int compare(String arg0, String arg1) {
          return arg0.toLowerCase().compareTo(arg1.toLowerCase());
        }
      };

  private static final Comparator<? super String> Comparator_SIZE =
      new Comparator<String>() {

        @Override
        public int compare(String arg0, String arg1) {
          File a = new File(arg0);
          File b = new File(arg1);

          if (a.isDirectory() && b.isDirectory()) {
            return arg0.compareTo(arg1);
          }

          if (a.isDirectory()) {
            return -1;
          }

          if (b.isDirectory()) {
            return 1;
          }

          final long lenA = a.length();
          final long lenB = b.length();

          if (lenA == lenB) {
            return arg0.toLowerCase().compareTo(arg1.toLowerCase());
          }

          if (lenA < lenB) {
            return -1;
          }

          return 1;
        }
      };

  private static final Comparator<? super String> Comparator_TYPE =
      new Comparator<String>() {

        @Override
        public int compare(String arg0, String arg1) {
          File a = new File(arg0);
          File b = new File(arg1);

          if (a.isDirectory() && b.isDirectory()) {
            return arg0.compareTo(arg1);
          }

          if (a.isDirectory()) {
            return -1;
          }

          if (b.isDirectory()) {
            return 1;
          }

          final String extA = FileUtils.getFileExtension(a.getName());
          final String extB = FileUtils.getFileExtension(b.getName());

          if (extA.isEmpty() && extB.isEmpty()) {
            return arg0.compareTo(arg1);
          }

          if (extA.isEmpty()) {
            return -1;
          }

          if (extB.isEmpty()) {
            return 1;
          }

          final int res = extA.compareTo(extB);
          if (res == 0) {
            return arg0.compareTo(arg1);
          }
          return res;
        }
      };

  private static final Comparator<? super String> Comparator_DATE =
      new Comparator<String>() {

        @Override
        public int compare(String arg0, String arg1) {
          Long first = new File(arg0).lastModified();
          Long second = new File(arg1).lastModified();

          return first.compareTo(second);
        }
      };
}
