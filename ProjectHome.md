Java ADB Explorer allows you to explore your Anroid Phone.


# I. Licence #

Project : Java ADB Explorer
Author : Ahmet DEMIR
Version 1.0
Date : May 2011
Description : Java ADB Explorer allows you to explore your Anroid Phone.
under License GPL: http://www.gnu.org/copyleft/gpl.html

---

Copyright (C) 2011 Ahmet DEMIR

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

**READ THE LICENCE IN THE DIRECTORY**

# II. INSTALLATION #

1. You must download first ADNROID SDK http://developer.android.com/sdk/index.html

2. Unzip it in and then make a symbolik link to ADB
```
sudo ln -s /DIRECTORY_TO_SDK/adb /usr/bin/adb
```

3. Install java6 and ant
For debian :
```
sudo apt-get install sun-java6-bin sun-java6-jre sun-java6-jdk 
sudo apt-get install ant
```

4. Where you run jADBExplorer, it creats a file "jadexplorer.log", you can change the destination in lib/log4j.cfg file (You see "CHANGE IT")

5. Run ADB with root privileges
```
adb kill-server
sudo adb start-server
```

6. run in the jADBExplorer directory
```
ant all
```
If all build success
```
ant run
```
You can install in your home (i.e. $HOME/.jadbexplorer/jadexplorer.jar)
```
ant install
```

# III. SCREENSHOT #

![http://java-adb-explorer.googlecode.com/files/screenshot-1.jpg](http://java-adb-explorer.googlecode.com/files/screenshot-1.jpg)
