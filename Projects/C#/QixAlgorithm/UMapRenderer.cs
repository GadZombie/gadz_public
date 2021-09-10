using QixAlgorithm;
using System;
using System.Windows.Media;
using System.Windows.Media.Imaging;

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
 * http://classic-games.pl, http://gad.art.pl, gad@gad.art.pl
 */

namespace QixAlgorithm
{
	public class TMapRenderer
	{
		public TMapRenderer()
		{
		}

		public void DrawMap(TMap Map, WriteableBitmap CBitmap, int CWidth, int CHeight)
		{
			double width, height;
			CBitmap.Clear(Colors.Black);

			width = (double)CWidth / (double)Map.GetWidth();
			height = (double)CHeight / (double)Map.GetHeight();
			for (int y = 0; y <= Map.GetHeight() - 1; y++)
				for (int x = 0; x <= Map.GetWidth() - 1; x++)
				{
					Color color;
					switch (Map.GetPoint(x, y))
					{
						case TMapPointType.pEmpty:
							color = Color.FromArgb(0xFF, 0x40, 0x20, 0x00);
							break;
						case TMapPointType.pLine:
							color = Color.FromArgb(0xFF, 0x30, 0x50, 0xdf);
							break;
						case TMapPointType.pFilled:
							color = Color.FromArgb(0xFF, 0x45, 0x98, 0x50);
							break;
						default:
							color = Colors.Black;
							break;
					}

					CBitmap.FillRectangle(
					   (int)Math.Floor(x * width), (int)Math.Floor(y * height),
						   (int)Math.Floor((x + 1) * width - 1), (int)Math.Floor((y + 1) * height - 1),
						   color);

					if (Map.GetOccupied(x, y))
					{
						CBitmap.FillEllipse(
							  (int)Math.Floor(x * width), (int)Math.Floor(y * height),
								 (int)Math.Floor((x + 1) * width - 1), (int)Math.Floor((y + 1) * height - 1),
								 Color.FromArgb(0xFF, 0xff, 0xef, 0x11)
						);
					}
				}
		}
	}
}

