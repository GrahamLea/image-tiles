
                                 Image  Tiles

                                 Version @VERSION@

                        http://image-tiles.dev.java.net/

LICENSE

Image Tiles is Copyright (c) 2004-2005 Graham Lea. All rights reserved.
Image Tiles is freely distributed under the Apache License 2.0.
See LICENSE.txt for details.


REQUIREMENTS

To run Image Tiles, you need a Java Runtime Environment (JRE) installed.
The version must be 1.4 or above.

                           Get Java   @   http://java.com/


INSTALL SCREENSAVER

To install the Image Tiles screensaver on un*x systems, follow these instructions:

  * Edit Makefile and jdkhome and xscreensaverhome to valid directories
  * Run 'make' to build the screensaver binaries for your platform
  * Copy files to the right directories.
      Java Desktop System:
        SCREENSAVER_BIN=/usr/lib/xscreensaver
        SCREENSAVER_CONF=/usr/lib/xscreensaver/config
      Solaris:
        SCREENSAVER_BIN=/usr/openwin/lib/xscreensaver
        SCREENSAVER_CONF=/usr/openwin/share/control-center-2.0/screensavers
      Red Hat 9:
        SCREENSAVER_BIN=/usr/X11R6/bin
        SCREENSAVER_CONF=/usr/share/control-center/screensavers
      Other platforms:
        SCREENSAVER_BIN=(search for an xscreensaver, like apollonian)
        SCREENSAVER_CONF=(search for a config file, like apollonian.xml)
    Please check where your other screensavers are installed.  You will
    need root access to do this (RPM will be provided later)
    1. Copy or symbolically link *.jar to SCREENSAVER_BIN
    2. Copy *.xml to SCREENSAVER_CONF
    3. For each screensaver, you will see two files, e.g.
       bouncingline and bouncingline-bin.  Copy or symbolically
       link bouncingline and bouncingline-bin to SCREENSAVER_BIN
  * Edit ~/.xscreensaver and add an entry for the screensaver.  For
    example, for BouncingLine, add the following to the programs section:
      "Bouncing Line (Java)" /full/path/to/bouncingline -root \n\
    (the bouncingline.bin, saverbeans-examples.jar saverbeans-api.jar files
    must appear in the same directory)
    NOTE: If you don't have a .xscreensaver file, go to your screensaver
    preferences and adjust the settings of a screensaver.  The file will
    be created for you automatically.
  * Make sure the Java Virtual Machine can be located by each screensaver
    from the shell launched by the xscreensaver process.
    The following sources will be checked for your Java Virtual Machine
    (in order).  See the screensaver wrapper script for more details.
    At worst, you can always edit these scripts directly, but usually
    editing ~/.xscreensaver and adding -jdkhome will suffice.
      - -jdkhome parameter, if present (this parameter is also set by the
        screensaver "Java Home" option in the control panel)
      - $JAVA_HOME environment variable, if set
      - `rpm -ql j2sdk`, if available
      - `which java`, if found
      - otherwise error
