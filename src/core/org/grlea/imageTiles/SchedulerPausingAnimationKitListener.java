package org.grlea.imageTiles;

// $Id: SchedulerPausingAnimationKitListener.java,v 1.1 2004-08-27 01:18:48 grlea Exp $
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

import org.grlea.util.FixedRateScheduler;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
SchedulerPausingAnimationKitListener
implements AnimationKitListener
{
   private static final int DEFAULT_DELAY = 5000;

   private final long delayMillis;

   private final DelayTask delayTask;

   private final FixedRateScheduler scheduler;

   public
   SchedulerPausingAnimationKitListener(FixedRateScheduler scheduler)
   {
      this(scheduler, DEFAULT_DELAY);
   }

   public
   SchedulerPausingAnimationKitListener(FixedRateScheduler scheduler, long delay)
   {
      if (scheduler == null)
         throw new IllegalArgumentException("scheduler cannot be null.");
      if (delay < 0)
         throw new IllegalArgumentException("delay cannot be < 0.");

      this.scheduler = scheduler;
      this.delayMillis = delay;
      delayTask = new DelayTask();
   }

   public void
   animationComplete()
   {
      if (delayMillis != 0)
      {
         scheduler.stop();
         Thread delayThread = new Thread(delayTask);
         delayThread.setDaemon(true);
         delayThread.start();
      }
   }

   private final class
   DelayTask
   implements Runnable
   {
      public void
      run()
      {
         long wakeUpTime = System.currentTimeMillis() + delayMillis;
         long sleepTime = delayMillis;
         while (sleepTime > 0)
         {
            try
            {
               Thread.sleep(sleepTime);
            }
            catch (InterruptedException ie)
            {
               // Shouldn't happen: we ignore it and loop anyway.
            }
            sleepTime = wakeUpTime - System.currentTimeMillis();
         }

         scheduler.start();
      }
   }
}