package org.grlea.imageTiles.pipeline;

// $Id: FrameRateListener.java,v 1.1 2004-09-04 07:59:25 grlea Exp $
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

/**
 * <p>A PipelineFrameLister that tracks the frame rate of the Pipeline and prints it every half
 * second.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
FrameRateListener
implements PipelineFrameListener
{
   private long lastUpdateTime = System.currentTimeMillis();

   private int previousFramesInHalfSecond = 0;
   private int framesInHalfSecond = 0;

   public
   FrameRateListener()
   {}

   public void
   advancedFrame(Pipeline pipeline)
   {
      framesInHalfSecond++;

      long currentTime = System.currentTimeMillis();
      if ((currentTime - lastUpdateTime) > 500)
      {
         int framesThisSecond = framesInHalfSecond + previousFramesInHalfSecond;
         System.out.println(framesThisSecond + " fps");

         previousFramesInHalfSecond = framesInHalfSecond;
         framesInHalfSecond = 0;
         lastUpdateTime = currentTime;
      }
   }
}