package org.grlea.imageTiles;

// $Id: ImageTilesDefaults.java,v 1.1 2004-09-04 07:59:17 grlea Exp $
// Copyright (c) 2004 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.grlea.imageTiles.transition.SlideTransition;
import org.grlea.imageTiles.background.ColourBackgroundPainter;
import org.grlea.imageTiles.choose.RandomChooser;
import org.grlea.imageTiles.choose.SequentialChooser;
import org.grlea.imageTiles.tileHolder.StaticTileHolderRenderer;
import org.grlea.imageTiles.place.CentrePlacer;
import org.grlea.imageTiles.place.ScalingCentrePlacer;
import org.grlea.imageTiles.render.PlainTileRenderer;

/**
 * <p>Constants and methods for easily obtaining default Image Tiles objects.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
ImageTilesDefaults
{
   public static int DEFAULT_FRAME_RATE = 50;

   public static int DEFAULT_TRANSITION_INTERVAL = 5000;

   public
   ImageTilesDefaults()
   {}

   public static TileHolderRenderer
   getDefaultPainter(TileSpace tileSpace)
   {
      return new StaticTileHolderRenderer(tileSpace);
   }

   public static Transition
   getDefaultTransition()
   {
      return new SlideTransition();
   }

   public static Chooser
   getDefaultChooser(TileSpace tileSpace)
   {
      return new RandomChooser(tileSpace);
   }

   public static BackgroundPainter
   getDefaultBackground(TileSpace tileSpace)
   {
      return new ColourBackgroundPainter(tileSpace);
   }

   public static TileRenderer
   getDefaultTileRenderer()
   {
      return new PlainTileRenderer();
   }

   public static Placer
   getDefaultPlacer()
   {
//      return new CentrePlacer();
      return new ScalingCentrePlacer();
   }
}