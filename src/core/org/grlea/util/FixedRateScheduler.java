package org.grlea.util;

// $Id: FixedRateScheduler.java,v 1.2 2004-08-27 01:03:01 grlea Exp $
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
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
FixedRateScheduler
{
   private final long period;

   private final Runnable task;

   private final boolean daemon;

   private Executor currentExecutor = null;

   private final Object currentExecutorLock = new Object();

   public
   FixedRateScheduler(Runnable task, int executionsPerSecond)
   {
      this(task, executionsPerSecond, true);
   }

   public
   FixedRateScheduler(Runnable task, int executionsPerSecond, boolean isDaemon)
   {
      this.task = task;
      this.period = (long) (1000F / executionsPerSecond);
      this.daemon = isDaemon;
   }

   public boolean
   isRunning()
   {
      synchronized (currentExecutorLock)
      {
         return currentExecutor != null && currentExecutor.running;
      }
   }

   public void
   start()
   {
      synchronized (currentExecutorLock)
      {
         if (currentExecutor != null)
            return;

         // TODO (grahaml) Maybe do something trickier than creating a new thread each time?
         currentExecutor = new Executor();
         Thread thread = new Thread(currentExecutor, "FixedRateScheduler");
         thread.setDaemon(daemon);
         thread.start();
      }
   }

   public void
   stop()
   {
      synchronized (currentExecutorLock)
      {
         if (currentExecutor == null || !currentExecutor.running)
            return;

         currentExecutor.running = false;
         currentExecutor = null;
      }
   }

   private final class
   Executor
   implements Runnable
   {
      private boolean running = true;

      public void
      run()
      {
         while (running)
         {
            // TODO (grahaml) May need to be fixed!? Not too accurate.

            long lastExecutionTime = System.currentTimeMillis();
            task.run();

            long nextFrameDrawTime = lastExecutionTime + period;
            long currentTime = System.currentTimeMillis();
            do
            {
               long timeToNextFrame = nextFrameDrawTime - currentTime;
               // Sleep 3/4 of the time:
               long timeToSleep = timeToNextFrame - (timeToNextFrame >> 2);
               if (timeToSleep > 0 )
               {
                  try
                  {
                     Thread.sleep(timeToSleep);
                  }
                  catch (InterruptedException ie)
                  {}
               }
               currentTime = System.currentTimeMillis();
            }
            while (currentTime < nextFrameDrawTime);
         }
      }
   }
}