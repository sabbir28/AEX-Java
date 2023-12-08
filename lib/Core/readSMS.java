import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ReadSMS {
    private Context context;

    public ReadSMS(Context context) {
        this.context = context;
    }

    public String readSMSBox(String box) {
        Uri uri;
        if (box.equals("inbox")) {
            uri = Uri.parse("content://sms/inbox");
        } else if (box.equals("sent")) {
            uri = Uri.parse("content://sms/sent");
        } else if (box.equals("draft")) {
            uri = Uri.parse("content://sms/draft");
        } else if (box.equals("outbox")) {
            uri = Uri.parse("content://sms/outbox");
        } else if (box.equals("failed")) {
            uri = Uri.parse("content://sms/failed");
        } else if (box.equals("queued")) {
            uri = Uri.parse("content://sms/queued");
        } else {
            throw new IllegalArgumentException("Invalid message box: " + box);
        }

        StringBuilder smsBuilder = new StringBuilder();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                // Append SMS details to the StringBuilder
                smsBuilder.append("Number: ").append(number).append("\n");
                smsBuilder.append("Person: ").append(person).append("\n");
                smsBuilder.append("Date: ").append(date).append("\n");
                smsBuilder.append("Body: ").append(body).append("\n\n");

            } while (cursor.moveToNext());
            cursor.close();
        }

        return smsBuilder.toString();
    }
}
