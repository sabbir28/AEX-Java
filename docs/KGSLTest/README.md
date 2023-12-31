# Android GPU Memory Manipulation with KGSLTest

This Android Studio project demonstrates GPU memory manipulation using the KGSLTest class. The KGSLTest class encapsulates functionality for GPU memory allocation, freeing, and running a child process.

## Prerequisites

- Android Studio installed on your machine.
- An Android device or emulator to run the application.

## Getting Started

1. Usage Examples:

    - **Allocate and Map GPU Memory:**

        ```java
        KGSLTest kgslTest = new KGSLTest();
        kgslTest.allocateAndMapMemory();
        ```

    - **Run Child Process:**

        ```java
        kgslTest.runChildProcess();
        ```

    - **Read from Pipe:**

        ```java
        kgslTest.readFromPipe();
        ```

    - **Free GPU Memory:**

        ```java
        kgslTest.freeMemory();
        ```

2. Build and Run the Application:

    - Connect your Android device or start an emulator.
    - Click the "Run" button in Android Studio to build and deploy the application.

3. View Logs:

    - Open Android Studio Logcat to view the logs generated by the application.
    - Filter logs with the tag `KGSLTest` to see relevant messages.

## Notes

- The KGSLTest class uses native methods for certain operations. Ensure your Android device or emulator supports these operations.
- Be cautious when manipulating GPU memory, as it may have security and stability implications.
- This example is for educational purposes, and the provided code may need adjustments based on your specific requirements and environment.