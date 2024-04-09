## Reproduce

- Install app on InkBook via Android Studio
- Start the app on the device
- Click on the permission request button to get the runtime permission for content access.
- Click on the button to select a folder (triggered using `OPEN_DOCUMENT_TREE` Intent action)
- Select a folder. If successful, the app will display the contents of the selected folder. But the app crashes instead with a security warning.

## Stacktrace

```
FATAL EXCEPTION: main
Process: com.example.permissionissueshowcase, PID: 11476
java.lang.RuntimeException: Failure delivering result ResultInfo{who=null, request=233396861, result=-1, data=Intent { dat=file:///storage/emulated/0 }} to activity {com.example.permissionissueshowcase/com.example.permissionissueshowcase.MainActivity}: java.lang.SecurityException: No persistable permission grants found for UID 10028 and Uri file:///storage/emulated/0 [user 0]
    at android.app.ActivityThread.deliverResults(ActivityThread.java:4268)
    at android.app.ActivityThread.handleSendResult(ActivityThread.java:4312)
    at android.app.ActivityThread.-wrap19(Unknown Source:0)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1644)
    at android.os.Handler.dispatchMessage(Handler.java:106)
    at android.os.Looper.loop(Looper.java:164)
    at android.app.ActivityThread.main(ActivityThread.java:6494)
    at java.lang.reflect.Method.invoke(Native Method)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:438)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:807)
Caused by: java.lang.SecurityException: No persistable permission grants found for UID 10028 and Uri file:///storage/emulated/0 [user 0]
    at android.os.Parcel.readException(Parcel.java:2013)
    at android.os.Parcel.readException(Parcel.java:1959)
    at android.app.IActivityManager$Stub$Proxy.takePersistableUriPermission(IActivityManager.java:7938)
    at android.content.ContentResolver.takePersistableUriPermission(ContentResolver.java:2085)
    at com.example.permissionissueshowcase.MainActivity$onCreate$1$launcher$1.invoke(MainActivity.kt:53)
    at com.example.permissionissueshowcase.MainActivity$onCreate$1$launcher$1.invoke(MainActivity.kt:50)
    at androidx.activity.compose.ActivityResultRegistryKt$rememberLauncherForActivityResult$1.invoke$lambda$0(ActivityResultRegistry.kt:106)
    at androidx.activity.compose.ActivityResultRegistryKt$rememberLauncherForActivityResult$1.$r8$lambda$li5zFnZIGklg6qYh4W157KrzSgE(Unknown Source:0)
    at androidx.activity.compose.ActivityResultRegistryKt$rememberLauncherForActivityResult$1$$ExternalSyntheticLambda0.onActivityResult(D8$$SyntheticClass:0)
    at androidx.activity.result.ActivityResultRegistry.doDispatch(ActivityResultRegistry.java:414)
    at androidx.activity.result.ActivityResultRegistry.dispatchResult(ActivityResultRegistry.java:371)
    at androidx.activity.ComponentActivity.onActivityResult(ComponentActivity.java:845)
    at android.app.Activity.dispatchActivityResult(Activity.java:7304)
    at android.app.ActivityThread.deliverResults(ActivityThread.java:4264)
    at android.app.ActivityThread.handleSendResult(ActivityThread.java:4312) 
    at android.app.ActivityThread.-wrap19(Unknown Source:0) 
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1644) 
    at android.os.Handler.dispatchMessage(Handler.java:106) 
    at android.os.Looper.loop(Looper.java:164) 
    at android.app.ActivityThread.main(ActivityThread.java:6494) 
    at java.lang.reflect.Method.invoke(Native Method) 
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:438) 
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:807)
```