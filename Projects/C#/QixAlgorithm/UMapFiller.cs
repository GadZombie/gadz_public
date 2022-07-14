using QixAlgorithm;
using System;

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
    public struct TPoint
    {
        public int X;
        public int Y;

        public TPoint(int X, int Y)
        {
            this.X = X;
            this.Y = Y;
        }
    }

    public class TMapFiller
    {
        private TMap MapRef;
        private TMap WorkMap;

        bool FoundOccupied;
        int CurrentId;

        public void FillMap(TMap Map)
        {
            TPoint point;
            MapRef = Map;

            WorkMap = new TMap();
            CopyMapToWorkMap();
            CurrentId = 0;

            do
            {
                point = FindStart();
                if (point.X >= 0)
                {
                    CurrentId++;
                    Start(point.X, point.Y);
                    CopyFill();
                };
            } while (point.X >= 0);
        }

        private void Finish()
        {
            WorkMap = null;
            MapRef = null;
        }

        TPoint FindStart()
        {
            for (int y = 0; y <= WorkMap.GetHeight() - 1; y++)

                for (int x = 0; x <= WorkMap.GetWidth() - 1; x++)
                {
                    if (WorkMap.GetPoint(x, y) == TMapPointType.pEmpty)
                    {
                        return new TPoint(x, y);
                    };
                }

            return new TPoint(-1, -1);
        }

        void Start(int px, int py)
        {
            FoundOccupied = false;
            FillPoint(px, py);
        }

        void FillPoint(int px, int py)
        {
            WorkMap.SetPoint(px, py, TMapPointType.pFilled);
            WorkMap.SetId(px, py, CurrentId);
            if (MapRef.GetOccupied(px, py))
                FoundOccupied = true;
            if (px > 0 && WorkMap.GetPoint(px - 1, py) == TMapPointType.pEmpty)
                FillPoint(px - 1, py);
            if (px < WorkMap.GetWidth() - 1 && WorkMap.GetPoint(px + 1, py) == TMapPointType.pEmpty)
                FillPoint(px + 1, py);
            if (py > 0 && WorkMap.GetPoint(px, py - 1) == TMapPointType.pEmpty)
                FillPoint(px, py - 1);
            if (py < WorkMap.GetHeight() - 1 && WorkMap.GetPoint(px, py + 1) == TMapPointType.pEmpty)
                FillPoint(px, py + 1);
        }

        void CopyFill()
        {

            if (FoundOccupied)
                return;

            for (int y = 0; y <= WorkMap.GetHeight() - 1; y++)
            {
                for (int x = 0; x <= WorkMap.GetWidth() - 1; x++)
                {
                    if (MapRef.GetPoint(x, y) == TMapPointType.pEmpty)
                    {
                        if (WorkMap.GetPoint(x, y) == TMapPointType.pFilled &&
                            WorkMap.GetId(x, y) == CurrentId)
                            MapRef.SetPoint(x, y, TMapPointType.pFilled);

                    }
                }
            }
        }

        void CopyMapToWorkMap()
        {
            WorkMap.Make(MapRef.GetWidth(), MapRef.GetHeight());
            for (int y = 0; y <= WorkMap.GetHeight() - 1; y++)
                for (int x = 0; x <= WorkMap.GetWidth() - 1; x++)
                    WorkMap.SetPoint(x, y, MapRef.GetPoint(x, y));
        }

    }
}