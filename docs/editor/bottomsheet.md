# Editor BottomSheet
Swipe up to open the BottomSheet.

## Build Output
In this tab, you can see the build outputs which were printed in the terminal while Gradle Build process.

## App Logs
Here you can view your App logs.<br/>
To see App logs use `startLogging(Context ctx)` method of `LogSender` class.<br/>
In your Activities or Application -
```java
...
import com.itsaky.androidide.logsender.LogSender;
...
    public void onCreate(Bundle savedInstanceState) {
        LogSender.startLogging(this);
        ...
    }
...
```

## IDE Logs
Here you can see the IDE logs.<br/>
IDE logs are logs from the IDE. By these logs, you can understand what is going on in the IDE. It is primarily used to debug IDE issues.

## Diagnostics
This tool help you to locate and identify syntax problems with the code. See errors and warnings from your code here.

## Search Results
You can see the results of `Find in project` here.