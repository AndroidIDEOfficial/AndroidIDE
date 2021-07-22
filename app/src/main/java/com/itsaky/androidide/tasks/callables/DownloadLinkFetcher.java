package com.itsaky.androidide.tasks.callables;

import androidx.core.util.Pair;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.utils.Either;
import java.util.List;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import org.json.JSONArray;
import android.text.TextUtils;
import com.blankj.utilcode.util.FileUtils;

public class DownloadLinkFetcher implements Callable<Either<List<Pair<String, String>>, String>> {

	@Override
	public Either<List<Pair<String, String>>, String> call() throws Exception {
		Document doc = Jsoup.connect(StudioApp.getInstance().getDownloadRequestUrl()).get();
		try {
			final List<Pair<String, String>> result = new ArrayList<>();
			JSONArray arr = new JSONArray(Jsoup.parse(doc.toString()).body().text());
			for(int i=0;i<arr.length();i++) {
				JSONObject o = arr.getJSONObject(i);
				result.add(Pair.create(o.getString("name"), o.getString("url")));
			}
			return Either.forLeft(result);
		} catch (JSONException th) {
			return Either.forRight(th.getMessage());
		}
	}
}
