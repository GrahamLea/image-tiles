
                                 Image  Tiles

                                 Version @VERSION@

                        http://image-tiles.dev.java.net/

LICENSE

Image Tiles is Copyright (c) 2004 Graham Lea. All rights reserved.
Image Tiles is freely distributed under the Apache License 2.0.
See LICENSE.txt for details.


REQUIREMENTS

To run Image Tiles, you need a Java Runtime Environment (JRE) installed.
The version must be 1.4 or above.

                           Get Java   @   http://java.com/


USAGE

The release comes with three demonstration applications:

[ SimplestDemo ]

Shows a single image being animated in a window.
To run SimplestDemo, either:

   * Double-click on 'imageTiles-simpleDemo.jar'
      OR
   * Run this command: java -jar imageTiles-simpleDemo.jar


[ InsideApplicationDemo ]

Demonstrates how Image Tiles components can be embedded into a Swing application.
To run InsideApplicationDemo, either:

   * Double-click on 'imageTiles-inApplicationDemo.jar'
      OR
   * Run this command: java -jar imageTiles-inApplicationDemo.jar


[ SimpleDemo ]

Shows some of the funkier rendering of tiles that can be performed, and also demonstrates Image
Tiles' image finding capabilities.
To run SimpleDemo:

   * Drag-and-Drop either an image or a directory containing images onto 'simpleDemo-dragNdrop.bat'
      OR
   * Run this command: start javaw -cp imageTiles-simpleDemo.jar org.grlea.imageTiles.demo.SimpleDemo

The second option will print instructions to the console about the parameters SimpleDemo allows.


STATUS

Though considerable work has been completed since Image Tiles was first released publicly, it is
still an alpha version. This is chiefly due to the fact that there are certain interactions within
the framework that are undesirable and are likely to change in the near future.

That being said, the changes that need to be made are mostly minor and all internal.
This means that development using the components in the swing package is unlikely to change.
Extensions to the framework may require a few semantic changes (method signatures and the like),
however the structure and division of responsibilities is unlikely to change.


CURRENT GOALS

The current immediate goals for Image Tiles are:

* Full-screen slideshow
* Screensaver
* Configuration file format & GUI tools


CHANGE LOG

0.3a

   + The TileSpace is now centered on the drawing canvas
   + DirectoryImageSource has more properties for controlling the order of images chosen
   + Added ImageTilesHelper.chooseAppropriateTileSize(Component)
   + Fixed transparent tile problem with PlainTileRenderer
   + Added the Image Tiles screensaver 

0.2a

   + Renamed Aniamtor to Transition
   + Refactored Painter out into TileHolder (class) and TileHolderRenderer (interface)
   + Animator.reset() is now Transition.newTransition(TileSet, TileSet, Chooser, TileHolder)
   + Changed Placer interface from:
        Point place(TileSet tileSet, BufferedImage sourceImage);
     to
        void  place(TileSet tileSet, BufferedImage sourceImage, Graphics targetGraphics);
   + Moved scaling operations out of Scaler implementations and into Placer implementations
   + Added DirectoryImageSource
   + Added ImageTilesDefaults
   + Added BackgroundPainter interface and implementations
   + Pipeline now performs frame timing internally and FixedRateScheduler is gone
   + Changed AnimationKit and RenderKit to be hidden classes rather than public interfaces
