import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadCallLogs {
    private final Context context;
    private final Activity activity;

    public ReadCallLogs(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public String readLogs() {
        StringBuilder callLogsBuilder = new StringBuilder();

        try {
            // Define the columns to retrieve from the call log
            String[] projection = new String[]{
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE
            };

            // Query the call log content provider
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC");

            // Check if there are call logs available
            if (cursor != null && cursor.getCount() > 0) {
                int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
                int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
                int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);

                // Iterate through the call logs
                while (cursor.moveToNext()) {
                    String number = cursor.getString(numberIndex);
                    String name = cursor.getString(nameIndex);
                    long dateMillis = cursor.getLong(dateIndex);
                    String duration = cursor.getString(durationIndex);
                    int callType = cursor.getInt(typeIndex);

                    // Convert date to a readable format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = dateFormat.format(new Date(dateMillis));

                    // Append the call log information to the StringBuilder
                    callLogsBuilder.append("Number: ").append(number).append("\n");
                    callLogsBuilder.append("Name: ").append(name).append("\n");
                    callLogsBuilder.append("Date: ").append(dateString).append("\n");
                    callLogsBuilder.append("Duration: ").append(duration).append("\n");

                    // Determine the call type
                    String callTypeString;
                    switch (callType) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callTypeString = "Incoming";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callTypeString = "Outgoing";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callTypeString = "Missed";
                            break;
                        default:
                            callTypeString = "Unknown";
                    }
                    callLogsBuilder.append("Type: ").append(callTypeString).append("\n");

                    // Add a separator between call logs
                    callLogsBuilder.append("------------------------------\n");
                }

                // Close the cursor
                cursor.close();
            } else {
                Log.d("ReadCallLogs", "No call logs found.");
            }

        } catch (SecurityException e) {
            Log.e("ReadCallLogs", "Permission to read call logs denied: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ReadCallLogs", "Error reading call logs: " + e.getMessage());
        }

        // Return the generated call logs string
        return callLogsBuilder.toString();
    }
}
