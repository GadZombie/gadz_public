using System;
using QixAlgorithm;

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
    public class TGame
    {
        public TMap Map;
        public TMapFiller MapFiller;

        public TGame()
        {
            MapFiller = new TMapFiller();
        }
        ~TGame() { }

        public void FillMap()
        {
            MapFiller.FillMap(Map);
        }

        void Finish()
        {
            Map = null;
        }

        public void New()
        {
            Map = new TMap();
            Map.Make(100, 80);

            //frame
            Map.DrawHLine(0, 0, Map.GetWidth() - 1, TMapPointType.pLine);
            Map.DrawVLine(0, 0, Map.GetHeight() - 1, TMapPointType.pLine);
            Map.DrawHLine(0, Map.GetHeight() - 1, Map.GetWidth() - 1, TMapPointType.pLine);
            Map.DrawVLine(Map.GetWidth() - 1, 0, Map.GetHeight() - 1, TMapPointType.pLine);

            //test path 1
            Map.DrawVLine(20, Map.GetHeight() - 1, Map.GetHeight() - 10, TMapPointType.pLine);
            Map.DrawHLine(20, Map.GetHeight() - 10, 35, TMapPointType.pLine);
            Map.DrawVLine(35, Map.GetHeight() - 10, Map.GetHeight() - 17, TMapPointType.pLine);
            Map.DrawHLine(35, Map.GetHeight() - 17, 43, TMapPointType.pLine);
            Map.DrawVLine(43, Map.GetHeight() - 17, Map.GetHeight() - 1, TMapPointType.pLine);

            Map.SetOccupied(15, 13, true);
            Map.SetOccupied(45, 33, true);

        }


    }
}