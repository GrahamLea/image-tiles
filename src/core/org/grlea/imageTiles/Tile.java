package org.grlea.imageTiles;

// $Id: Tile.java,v 1.2 2004-09-04 07:59:19 grlea Exp $
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
 * <p>One Tile in a TileSpace. The tile is merely a record of the position of a single tile
 * within the tile space. The position is recorded both as a row/column pair and as the x/y offset
 * from the top-left corner of the tile space.</p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
Tile
implements Comparable
{
   public final int column;

   public final int row;

   public final int x;

   public final int y;

   public
   Tile(int column, int row, int x, int y)
   {
      this.column = column;
      this.row = row;
      this.x = x;
      this.y = y;
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