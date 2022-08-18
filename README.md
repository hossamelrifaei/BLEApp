# Tech Test for Android

This test is about scanning and connecting to a bluetooth device and interacting with its services and characteristics.

## Goals

- Scan for BLE Devices with the advertised service listed in in the **BLE Services Specification** below
- Connect to the Device
- List the **Services** and **Characteristics** on that Device

## Implementation Details
- Implement Landing Activity that allows for scanning and display of BLE devices
- Detail Activity that displays connection status and values from **BLE Services Specification** below
- Foreground service that manages connection and data exchange

## User Stories

1. Scan for devices (Landing Activity)

    As a user I want to be able to see the list of discovered BLE devices after tapping the scan button

2. Selecting a device (Landing Activity)

   As a user I want to be able to select a device I want to connect to by tapping on it from the list

3. Connect to device (Detail Activity and Foreground Service)

    As a user I want to be able to see the connection status while connection is being established by the Foreground Service. After the connection is established, I want to be able to see the BLE device's services and characteristics data in the Device Activity.

4. Device Detail (Device Activity and Foreground Service)

    As a user I want to be able to see the characteristics data in the Device Activity parsed by the Foreground Service using the **BLE Services Specification** format

5. Disconnection (Landing Activity)

    As a user I want to go back to the Landing Activity automatically when the BLE device gets disconnected

## How to start 

1. Download the companion App from the link sent to you
2. Run the App and grant the Bluetooth permissions to start advertising the Services described in the **BLE Services Specification** below
3. If you want to confirm the service is advertising you can download [NRF Connect](https://apps.apple.com/us/app/nrf-connect-for-mobile/id1054362403) on your phone to verify if you can see the service advertising from your device.
4. From here you are ready to start

## As a general rule, please

- Avoid using Third Party BLE Libraries
- Use MVVM as much as possible

## Nice to have

- Unit tests

___
# BLE Services Specification

- GATT service with this UUID: `4576d562-7e68-11ec-90d6-0242ac120003`
- The service contains the following characteristics
    1. Int32 Characteristic (Little-Endian)
        - UUID: `9699ee02-7e68-11ec-90d6-0242ac120003`
        - Notifiable and Read Only
        - Updates every second
    2. Int32 Characteristic (Big-Endian)
        - UUID: `e187c2ac-7f28-11ec-a8a3-0242ac120002`
        - Notifiable and Read Only
        - Updates every second
    3. String Characteristic
        - UUID: `b8994dbc-8224-11ec-a8a3-0242ac120002`
        - Read Only
        - First 2 bytes (little endian) contains the length of the encoded 1st string
        - Succeeding bytes until the 1st string length contains the 1st string encoded in UTF-8
        - Th next 2 bytes (little endian) contains the length of the encoded 2nd string
        - Succeeding bytes until the 2nd string length contains the 2nd string encoded in UTF-8
    4. PNG Characteristic
        - UUID: `5429d9b2-82b7-11ec-a8a3-0242ac120003`
        - Read Only
        - Represents a PNG encoded image


## Troubleshooting
Since the Bluetooth connection of the companion app is managed by macOS, to simulate disconnection, you have to turn off your Mac Bluetooth.
