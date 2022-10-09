# Installation
This guide shows how to install AndroidIDE, setting up the terminal and the Android build tools installation.

Before getting started, please make sure that your Android device meets the minimum requirements.

## Minimum requirements
- ARM based CPU `arm64-v8a` (a.k.a aarch64) or `armeabi-v7a`.
- At least 1.5GB of RAM **_available for use_** (not total RAM).
- At least 4GB of free space is recommended. Around 1.5GB for installation and rest for other dependencies that you might need to download depending on your project.

## Setup the terminal [^cheat]
- Open AndroidIDE terminal. It will install the bootstrap packages.
- Then run `pkg update` to update packages to latest version.

## Build Tools Installation [^cheat]
Install the JDK, SDK and commandline tools for sdk.
- Run `pkg install wget` to install `wget`.
- Get the installation script with :
    ```bash
    wget https://raw.githubusercontent.com/AndroidIDEOfficial/androidide-build-tools/main/install.sh
    ```
- Give executable permissions to the installation script with:
    ```bash
    chmod +x ./install.sh
    ```
- Start the installation process by executing the script with :
    ```
    ./install.sh -c
    ```
    Execute the script with `-h` argument to see a list of options you can use to customize the installation.
- After you execute the script, it'll show a summary of the configuration. Type `y` to confirm the configuration and start the installation process.
- After successful installation, `Downloads completed. You are ready to go!` will be printed.

[^cheat]:
    Cheatsheet : 
    - One command to setup the terminal and to install build tools.
        ```bash
        cd && pkg up && pkg in wget && wget https://raw.githubusercontent.com/AndroidIDEOfficial/androidide-build-tools/main/install.sh && chmod +x ./install.sh && ./install.sh -c
        ```
