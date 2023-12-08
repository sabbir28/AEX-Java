# readSMS Class Documentation

The `readSMS` class provides functionality to retrieve SMS messages from various message boxes on an Android device.

## Constructor

### `readSMS(Context context)`

Initializes a new instance of the `readSMS` class.

#### Parameters
- `context`: The application context.

## Methods

### `readSMSBox(String box)`

Retrieves SMS messages from the specified message box.

#### Parameters
- `box`: The message box to read from. Possible values are:
    - `"inbox"`: Retrieve SMS messages from the inbox.
    - `"sent"`: Retrieve SMS messages from the sent box.
    - `"draft"`: Retrieve SMS messages from the draft box.
    - `"outbox"`: Retrieve SMS messages from the outbox.
    - `"failed"`: Retrieve SMS messages from the failed box.
    - `"queued"`: Retrieve SMS messages from the queued box.

#### Returns
- A string containing the retrieved SMS messages in the following format:
    - `Number: <number>`
    - `Person: <person>`
    - `Date: <date>`
    - `Body: <body>`
    - (repeated for each SMS message)

## Usage

To use the `readSMS` class, follow these steps:

1. Create an instance of `readSMS` by passing the application context to the constructor:
    ```java
    readSMS smsReader = new readSMS(getApplicationContext());
    ```

2. Read SMS messages from a specific message box using the `readSMSBox()` method:
    ```java
    String inboxSMS = smsReader.readSMSBox("inbox"); // Read SMS messages from the inbox
    ```

3. The returned SMS messages will be in the form of a string, containing information such as the number, person, date, and body of each message.

## Data Provided

The `readSMS` class provides the following information for each retrieved SMS message:

- `Number`: The phone number associated with the message.
- `Person`: The contact name (if available) associated with the message.
- `Date`: The timestamp indicating when the message was received/sent.
- `Body`: The content/body of the SMS message.

Please note that accessing SMS data might require appropriate permissions in the Android manifest file.
