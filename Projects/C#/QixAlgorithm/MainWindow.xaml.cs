using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

/* This program was downloaded from http://classic-games.pl.
 * It's an example of filling algorithm as seen in the game Qix or Volfied.
 * 
 * You may use this code in any way but if you create any application or game using
 * this code (a part or the whole) you should mention about the author of original code,
 * that is Grzegorz Drozd - and the source website http://classic-games.pl.
 * That's all. If you want, that will be an honour for me if you write to me and tell
 * about it! Have fun.
 * 
 * Please, download the WriteableBitmapEx.1.6.2 from NuGet if you want to compile this project.
 *
 * (c) 2019-04-14 Grzegorz Drozd, 
 * http://classic-games.pl, http://gad.art.pl, gad@gad.art.pl
 */

namespace QixAlgorithm
{
    public partial class MainWindow : Window
    {
        private TMapRenderer MapRenderer;
        public MainWindow()
        {
            InitializeComponent();

            this.SizeChanged += ImageSizeChangedEventHandler;
            this.MouseMove += ImageMouseMove;
            this.MouseDown += ImageMouseDown;

            MapRenderer = new TMapRenderer();
            TGameFactory.CurrentGame = TGameFactory.CreateGame();
            TGameFactory.CurrentGame.New();
            TGameFactory.CurrentGame.FillMap();

            MapRepaint();
        }

        void MapRepaint()
        {
            int width = (int)pic.ActualWidth;
            int height = (int)pic.ActualHeight;
            MainImage.Width = width;
            MainImage.Height = height;

            WriteableBitmap pBitmap = BitmapFactory.New(width, height);
            Canvas cnvPoints = new Canvas();
            cnvPoints.Width = width;
            cnvPoints.Height = height;

            if (MapRenderer != null)
                MapRenderer.DrawMap(TGameFactory.CurrentGame.Map, pBitmap, width, height);
            this.MainImage.Source = pBitmap;
        }

        void ImageSizeChangedEventHandler(object sender, SizeChangedEventArgs e)
        {
            MapRepaint();
        }

        void ImageMouseMove(object sender, MouseEventArgs e)
        {
            int width = (int)pic.ActualWidth;
            int height = (int)pic.ActualHeight;

            try
            {
                Point pt = e.MouseDevice.GetPosition(pic);
                TPoint mpt = new TPoint();
                mpt.X = (int)Math.Floor(pt.X / ((double)width / TGameFactory.CurrentGame.Map.GetWidth()));
                mpt.Y = (int)Math.Floor(pt.Y / ((double)height / TGameFactory.CurrentGame.Map.GetHeight()));
                TGameFactory.CurrentGame.Map.SetPoint(mpt.X, mpt.Y, TMapPointType.pLine);
            }
            catch (Exception)
            {
            }
            MapRepaint();
        }

        void ImageMouseDown(object sender, MouseEventArgs e)
        {
            TGameFactory.CurrentGame.FillMap();
            MapRepaint();
        }

    }
}

