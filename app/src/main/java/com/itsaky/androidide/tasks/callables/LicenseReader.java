package com.itsaky.androidide.tasks.callables;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsaky.androidide.models.License;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;

public class LicenseReader implements Callable<List<License>> {
    
    private final Context ctx;

    public LicenseReader(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public List<License> call() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("licenses.json")));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }
        TypeToken<List<License>> token = new TypeToken<List<License>>(){};
        return new Gson().fromJson(sb.toString(), token.getType());
    }
}
