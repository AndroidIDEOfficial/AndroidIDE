/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.sdk;

import android.content.Context;
import androidx.annotation.NonNull;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;
import java.util.Objects;

/**
 * Class that holds all of the 'sdk-info' APIs.
 *
 * @see ApiInfo
 * @see AttrInfo
 * @see WidgetInfo
 * @author Akash Yadav
 */
public class SDKInfo {

  public AttrInfo getAttrInfo() {
    return attrInfo;
  }

  public ApiInfo getApiInfo() {
    return apiInfo;
  }

  public WidgetInfo getWidgetInfo() {
    return widgetInfo;
  }

  private final AttrInfo attrInfo;
  private final ApiInfo apiInfo;
  private final WidgetInfo widgetInfo;

  public SDKInfo(@NonNull final Context context) throws Exception {
    Objects.requireNonNull(context);
    this.apiInfo = new ApiInfo(context);
    this.attrInfo = new AttrInfo(context);
    this.widgetInfo = new WidgetInfo(context);
  }
}
