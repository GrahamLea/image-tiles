package org.grlea.imageTiles;

// $Id: AnimationRenderKit.java,v 1.1 2004-08-27 01:17:47 grlea Exp $
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

import java.awt.Graphics2D;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
AnimationRenderKit
implements RenderKit
{
   private final BackgroundPainter background;

   private final Animator animator;

   private final Painter painter;

   public
   AnimationRenderKit(BackgroundPainter background, Animator animator, Painter painter)
   {
      this.background = background;
      this.animator = animator;
      this.painter = painter;
   }

   public void
   render(Graphics2D graphics)
   {
      background.paintBackground(graphics);
      painter.render(graphics);
      animator.render(graphics);
   }
}