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
    public class TGameFactory
    {
        public static TGame CurrentGame;

        static public TGame CreateGame()
        {
            return new TGame();
        }

    }
}