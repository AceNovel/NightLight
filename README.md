# Night Light
[![Build Status](https://travis-ci.org/corphish/NightLight.svg?branch=master)](https://travis-ci.org/corphish/NightLight)
[![Donate](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.me/corphish)

[<img alt='Get it on Google Play'  src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
height="80"/>](https://play.google.com/store/apps/details?id=com.corphish.nightlight.generic)
[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/packages/com.corphish.nightlight.generic/)

Night light uses KCAL to adjust blue light intensity of the display colors, so that viewing the screen at dark becomes pleasant for the eyes, and help you fall asleep faster (this is what science have proven so...).

### What is KCAL?
KCAL is a display driver tuning feature for Qualcomm devices, written by `savoca`. Although this feature is most likely not shipped in the stock kernels of various custom ROMs, it should be available in the custom kernels available for your device, provided that your device runs on any 64-bit Qualcomm processor. To see the full list of supported qcom SoCs, visit [this thread](https://forum.xda-developers.com/android/software-hacking/dev-kcal-advanced-color-control-t3032080).

### Why on earth did I make it?
I flashed a LineageOS oreo build on my device, but there was no night light feature available on the ROM. The build itself was early so there was no LiveDisplay, and the Android Oreo night light feature was not their as it is Pixel specific. Luckily, the kernel shipped with the build had KCAL, so I decided to harness this feature. But I had to everytime use a kernel manager app, and had to adjust the intensity, hence I decided to make an app that would make this easier for me.

### Features
* Control blue light filter intensity to your liking.
* Control green light filter to add reddish tint.
* Or, use color temperature setting to tune Night Light.
* Easy one touch toggles, with a single slider to tweak blue light intensity.
* Quick Setting tile for easy toggling on/off night light anywhere.
* Automatic switching on/off night light at user specified timings.
* Supports sunset/sunrise timings.
* Set on boot delay.
* Support for user profiles, which are collections of settings that user can apply with one click.
* And to fulfill your all kinds of automation needs, app is supported as a Tasker plugin. You can use it with Profiles.
* Material design.

### Requirements
* Kernel supporting KCAL.
* Root access.

### Permissions
* Location - Needed to determine sunset/sunrise time for your current location.
* Internet - Internet connection is needed to determine coarse location when fine location is not available.
