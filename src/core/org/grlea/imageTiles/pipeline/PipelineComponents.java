package org.grlea.imageTiles.pipeline;

// $Id: PipelineComponents.java,v 1.1 2004-09-04 07:59:26 grlea Exp $
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

import org.grlea.imageTiles.BackgroundPainter;
import org.grlea.imageTiles.Chooser;
import org.grlea.imageTiles.ImageTilesDefaults;
import org.grlea.imageTiles.TileHolderRenderer;
import org.grlea.imageTiles.Placer;
import org.grlea.imageTiles.TileRenderer;
import org.grlea.imageTiles.TileSpace;
import org.grlea.imageTiles.Transition;
import org.grlea.imageTiles.ImageTilesHelper;
import org.grlea.imageTiles.ImageSource;
import org.grlea.imageTiles.imageSource.SingleImageSource;

import java.awt.image.BufferedImage;

/**
 * <p>Defines all the components required by a Pipeline.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
PipelineComponents
{
   final TileSpace tileSpace;

   final ImageSource imageSource;

   final Placer placer;

   final TileRenderer tileRenderer;

   final AnimationKit animationKit;

   final RenderKit renderKit;

   public
   PipelineComponents(BufferedImage image)
   {
      this(ImageTilesHelper.createTileSpace(image), new SingleImageSource(image));
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource)
   {
      this(tileSpace, imageSource, null, null, null, null, null, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, TileRenderer tileRenderer)
   {
      this(tileSpace, imageSource, tileRenderer, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, Placer placer)
   {
      this(tileSpace, imageSource, null, placer, null, null, null, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, Chooser chooser)
   {
      this(tileSpace, imageSource, null, null, chooser, null, null, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource,
                      BackgroundPainter backgroundPainter)
   {
      this(tileSpace, imageSource, null, null, null, backgroundPainter, null, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource,
                      TileHolderRenderer tileHolderRenderer)
   {
      this(tileSpace, imageSource, null, null, null, null, tileHolderRenderer, null);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, Transition transition)
   {
      this(tileSpace, imageSource, null, transition);
   }

   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, TileRenderer tileRenderer,
                      Transition transition)
   {
      this(tileSpace, imageSource, tileRenderer, null, null, null, null, transition);
   }

   /**
    * Creates a new <code>PipelineComponents</code> object using the given objects. Any of the
    * arguments except for <code>tileSpace</code> and <code>imageSource</code> may be
    * <code>null</code>. Those that are <code>null</code> will be replaced by the corresponding
    * default object returned from {@link ImageTilesDefaults}.
    */
   public
   PipelineComponents(TileSpace tileSpace, ImageSource imageSource, TileRenderer tileRenderer,
                      Placer placer, Chooser chooser, BackgroundPainter backgroundPainter,
                      TileHolderRenderer tileHolderRenderer, Transition transition)
   {
      if (tileSpace == null)
         throw new IllegalArgumentException("tileSpace cannot be null.");
      if (imageSource == null)
         throw new IllegalArgumentException("imageSource cannot be null.");

      if (placer == null)
         placer = ImageTilesDefaults.getDefaultPlacer();
      if (tileRenderer == null)
         tileRenderer = ImageTilesDefaults.getDefaultTileRenderer();
      if (chooser == null)
         chooser = ImageTilesDefaults.getDefaultChooser(tileSpace);
      if (backgroundPainter == null)
         backgroundPainter = ImageTilesDefaults.getDefaultBackground(tileSpace);
      if (tileHolderRenderer == null)
         tileHolderRenderer = ImageTilesDefaults.getDefaultPainter(tileSpace);
      if (transition == null)
         transition = ImageTilesDefaults.getDefaultTransition();

      this.tileSpace = tileSpace;
      this.imageSource = imageSource;
      this.placer = placer;
      this.tileRenderer = tileRenderer;
      this.animationKit = new AnimationKit(chooser, transition, tileHolderRenderer);
      this.renderKit = new RenderKit(backgroundPainter, transition, tileHolderRenderer);
   }

   public TileSpace
   getTileSpace()
   {
      return tileSpace;
   }
}