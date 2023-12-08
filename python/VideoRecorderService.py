import subprocess
import time
import re
import os

def get_connected_devices():
    try:
        # Run ADB devices command to list connected devices
        result = subprocess.run('adb devices', shell=True, check=True, text=True, capture_output=True)
        device_list = re.findall(r'\b(\w+)\tdevice\b', result.stdout)

        return device_list

    except subprocess.CalledProcessError as e:
        print(f"Error: {e}")
        print(f"Output: {e.output}")
        return []

def get_available_cameras(device_id):
    try:
        # Run ADB command to list available cameras
        adb_command = f'adb -s {device_id} shell "pm list features"'
        result = subprocess.run(adb_command, shell=True, check=True, text=True, capture_output=True)

        # Check for the existence of camera features
        front_camera = 'android.hardware.camera.front' in result.stdout
        back_camera = 'android.hardware.camera' in result.stdout

        # Return a list of available cameras
        cameras = []
        if front_camera:
            cameras.append('front')
        if back_camera:
            cameras.append('back')

        return cameras

    except subprocess.CalledProcessError as e:
        print(f"Error: {e}")
        print(f"Output: {e.output}")
        return []

def start_video_recording(device_id, camera_id, output_file='output.mp4', duration_seconds=10):
    try:
        # Start video recording using ADB command
        adb_command = f'adb -s {device_id} shell screenrecord --time-limit {duration_seconds} --output-format mp4 /sdcard/{output_file}'
        print(f"Starting video recording on device {device_id} using camera {camera_id}")
        subprocess.run(adb_command, shell=True, check=True)

        # Sleep for the specified duration (you can perform other tasks during recording)
        print(f"Recording in progress... Sleeping for {duration_seconds} seconds.")
        time.sleep(duration_seconds)

        # Stop video recording using ADB command
        stop_adb_command = f'adb -s {device_id} shell pkill -l 3 -SIGINT screenrecord'
        print("Stopping video recording.")
        subprocess.run(stop_adb_command, shell=True, check=True)

        # Pull the recorded video file from the device
        pull_command = f'adb -s {device_id} pull /sdcard/{output_file} .'
        print(f"Pulling recorded video file from device {device_id}.")
        subprocess.run(pull_command, shell=True, check=True)

        print(f"Video recorded successfully and saved as {output_file}")

    except subprocess.CalledProcessError as e:
        print(f"Error: {e}")
        print(f"Output: {e.output}")

def main():
    # Get a list of connected devices
    devices = get_connected_devices()

    if not devices:
        print("No connected devices found.")
        return

    # Use the first connected device
    device_id = devices[0]
    print(f"Selected device: {device_id}")

    # Get available cameras on the device
    cameras = get_available_cameras(device_id)

    if not cameras:
        print("No available cameras found on the device.")
        return

    # Use the first available camera for recording
    camera_id = cameras[0]
    print(f"Selected camera: {camera_id}")

    # Specify the output file name and duration of recording
    output_file_name = '1recorded_video.mp4'
    recording_duration = 60  # in seconds

    start_video_recording(device_id, camera_id, output_file_name, recording_duration)

if __name__ == "__main__":
    main()
