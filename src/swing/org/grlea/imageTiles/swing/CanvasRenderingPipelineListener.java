package org.grlea.imageTiles.swing;

// $Id: CanvasRenderingPipelineListener.java,v 1.1 2004-09-04 07:59:37 grlea Exp $
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

import org.grlea.imageTiles.swing.CanvasPipelineRenderer;
import org.grlea.imageTiles.pipeline.PipelineFrameListener;
import org.grlea.imageTiles.pipeline.Pipeline;

import java.awt.Canvas;

/**
 * <p>An extension of {@link CanvasPipelineRenderer} that listens to a Pipeline in order to render
 * the pipeline to the canvas once per frame.</p>
 *
 * @see AnimatedTileCanvas
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
CanvasRenderingPipelineListener
extends CanvasPipelineRenderer
implements PipelineFrameListener
{
   public
   CanvasRenderingPipelineListener(Canvas canvas)
   {
      super(canvas);
   }

   public void
   advancedFrame(Pipeline pipeline)
   {
      render(pipeline);
   }
}