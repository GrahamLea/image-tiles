package org.grlea.imageTiles.swing;

// $Id: AnimatedTileCanvasRenderTask.java,v 1.1 2004-08-23 05:01:47 grlea Exp $
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

import org.grlea.imageTiles.AnimationController;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
AnimatedTileCanvasRenderTask
implements Runnable
{
   private final AnimationController controller;

   private final TileCanvas canvas;

   public
   AnimatedTileCanvasRenderTask(AnimationController controller, TileCanvas canvas)
   {
      this.controller = controller;
      this.canvas = canvas;
   }

   public void
   run()
   {
      controller.advanceFrame();
      canvas.render();
   }
}