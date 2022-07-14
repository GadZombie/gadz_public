using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/* This program was downloaded from http://classic-games.pl.
 * It's an example of filling algorithm as seen in the game Qix or Volfied.
 * 
 * You may use this code in any way but if you create any application or game using
 * this code (a part or the whole) you should mention about the author of original code,
 * that is Grzegorz Drozd - and the source website http://classic-games.pl.
 * That's all. If you want, that will be an honour for me if you write to me and tell
 * about it! Have fun.
 * 
 * (c) 2019-04-14 Grzegorz Drozd, 
 * http://classic-games.pl, https://gadz.pl, gad@gadz.pl
 */


namespace QixAlgorithm
{
    public enum TMapPointType { pEmpty, pLine, pFilled };

    public class TMapPoint {

        public TMapPointType Value;
        public bool Occupied;
        public int id;

        public TMapPoint()
        {
            Value = TMapPointType.pEmpty;
            Occupied = false;
            id = 0;
        }
    }

    public class TMapRow : List<TMapPoint> { };
    public class TMapMatrix : List<TMapRow> { };

    public class TMap {
        private int Width, Height;
        private TMapMatrix points;

        private void Swap(ref int v1, ref int v2)
        {
            int tmp;
            tmp = v1;
            v1 = v2;
            v2 = tmp;
        }

        private void CheckPosition(int px, int py) {
            if (px < 0 || py < 0 || px >= this.Width || py >= this.Height)
                throw new Exception(String.Format("Map point out of range [{0}x{1}]", px, py));
        }

        public TMap()
        {
            points = new TMapMatrix();
        }

        public void DrawHLine(int x1, int y, int x2, TMapPointType Value)
        {
            if (x1 > x2)
                Swap(ref x1, ref x2);
            for (int x = x1; x <= x2; x++)
                SetPoint(x, y, Value);
        }

        public void DrawVLine(int x, int y1, int y2, TMapPointType Value)
        {
            if (y1 > y2)
                Swap(ref y1, ref y2);
            for (int y = y1; y <= y2; y++)
                SetPoint(x, y, Value);
        }

        public void Make(int sx, int sy)
        {

            TMapRow MapRow;
            this.Width = sx;
            this.Height = sy;

            points.Clear();
            for (int y = 0; y <= sy - 1; y++)
            {
                MapRow = new TMapRow();
                for (int x = 0; x <= sx - 1; x++)
                    MapRow.Add(new TMapPoint());

                points.Add(MapRow);
            }

        }

        public void SetId(int px, int py, int Value)
        {
            CheckPosition(px, py);
            points[py][px].id = Value;
        }

        public void SetOccupied(int px, int py, bool Value)
        {
            CheckPosition(px, py);
            points[py][px].Occupied = Value;
        }

        public void SetPoint(int px, int py, TMapPointType Value)
        {
            CheckPosition(px, py);
            points[py][px].Value = Value;
        }

        public int GetHeight()
        {
            return Height;
        }

        public int GetId(int px, int py)
        {
            CheckPosition(px, py);
            return points[py][px].id;
        }

        public bool GetOccupied(int px, int py)
        {
            CheckPosition(px, py);
            return points[py][px].Occupied;
        }

        public TMapPointType GetPoint(int px, int py)
        {
            CheckPosition(px, py);
            return points[py][px].Value;
        }

        public int GetWidth()
        {
            return Width;
        }

    }

}
