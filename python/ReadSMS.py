import argparse
import subprocess
import logging

class ReadSMS:
    def __init__(self, args):
        self.args = args

        # Set up logging
        self.setup_logging()

        # Check if any device is connected
        if not self.check_device_connected():
            logging.error("No device connected via ADB.")
            return

        # Build the ADB command based on provided arguments
        self.adb_command = self.build_adb_command()

    def setup_logging(self):
        # Configure logging to print log messages to the console
        logging.basicConfig(level=logging.INFO, format='%(asctime)s [%(levelname)s]: %(message)s')

    def check_device_connected(self):
        try:
            # Run ADB devices command to list connected devices
            result = subprocess.run("adb devices", shell=True, check=True, text=True, capture_output=True)

            # Check if any device is listed in the output
            device_connected = "List of devices attached" in result.stdout

            # Log device connection status
            logging.info(f"Device connected: {device_connected}")

            return device_connected

        except subprocess.CalledProcessError as e:
            logging.error(f"Error checking device connection: {e}")
            return False

    def build_adb_command(self):
        command = "adb shell content query"

        # Specify URI based on the provided content provider
        command += f" --uri {self.args.provider}" if self.args.provider else ""

        # Add projection if specified
        command += f" --projection {self.args.projection}" if self.args.projection else ""

        # Add selection if specified
        command += f" --where {self.args.where}" if self.args.where else ""

        return command

    def read_sms(self):
        try:
            # Run the ADB command
            result = subprocess.run(self.adb_command, shell=True, check=True, text=True, capture_output=True)

            # Print the result
            print(result.stdout)

            # Log successful execution
            logging.info("ADB command executed successfully.")

        except subprocess.CalledProcessError as e:
            logging.error(f"Error: {e}")
            logging.error(f"Output: {e.output}")

if __name__ == "__main__":
    # Define command-line arguments
    parser = argparse.ArgumentParser(description="Read SMS messages from an Android device using ADB.")
    parser.add_argument("--provider", help="Specify the complete content provider URI (e.g., content://sms/inbox)")
    parser.add_argument("--projection", help="Specify the projection for the query")
    parser.add_argument("--where", help="Specify the selection criteria for the query")

    # Parse command-line arguments
    args = parser.parse_args()

    # Display help information if no arguments are provided
    if not any(vars(args).values()):
        parser.print_help()
    else:
        # Create an instance of ReadSMS with the parsed arguments
        sms_reader = ReadSMS(args)

        # If a device is connected, read SMS messages
        if sms_reader.check_device_connected():
            sms_reader.read_sms()
