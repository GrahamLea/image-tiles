package org.grlea.imageTiles;

// $Id: Tile.java,v 1.1 2004-08-23 22:47:38 grlea Exp $
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
 * @version $Revision: 1.1 $
 */
public class
Tile
implements Comparable
{
   private final int column;

   private final int row;

   private final int x;

   private final int y;

   public
   Tile(int column, int row, int x, int y)
   {
      this.column = column;
      this.row = row;
      this.x = x;
      this.y = y;
   }

   public int
   getColumn()
   {
      return column;
   }

   public int
   getRow()
   {
      return row;
   }

   public int
   getX()
   {
      return x;
   }

   public int
   getY()
   {
      return y;
   }

   public int
   hashCode()
   {
      return (x << 16) | y;
   }

   public boolean
   equals(Object o)
   {
      if (o == this)
         return true;

      if (!(o instanceof Tile))
         return false;

      Tile that = ((Tile) o);
      return this.x == that.x && this.y == that.y;
   }

   public int
   compareTo(Object o)
   {
      if (o == this)
         return 0;

      Tile that = ((Tile) o);
      int xDiff = this.x - that.x;
      return ((xDiff == 0) ? (this.y - that.y) : xDiff);
   }
}