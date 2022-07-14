unit Main;

interface

{* This program was downloaded from http://classic-games.pl.
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
 *}

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ExtCtrls,

  UMapRenderer;

type
  TfrmMain = class(TForm)
    pntScreen: TPaintBox;
    procedure pntScreenPaint(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure pntScreenMouseMove(Sender: TObject; Shift: TShiftState; X,
      Y: Integer);
    procedure pntScreenMouseDown(Sender: TObject; Button: TMouseButton;
      Shift: TShiftState; X, Y: Integer);
    procedure FormShow(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
  private
    { Private declarations }
    function MouseToMap(MousePoint: TPoint): TPoint;
  public
    { Public declarations }
    MapRenderer: TMapRenderer;
  end;

var
  frmMain: TfrmMain;

implementation
uses
  UGameFactory, UMap;

{$R *.dfm}

procedure TfrmMain.FormCreate(Sender: TObject);
begin
  Self.DoubleBuffered := true;
  MapRenderer := TMapRenderer.Create;
end;

procedure TfrmMain.FormDestroy(Sender: TObject);
begin
  MapRenderer.Free;
end;

procedure TfrmMain.FormShow(Sender: TObject);
begin
  CurrentGame.New;
  CurrentGame.FillMap;
end;

function TfrmMain.MouseToMap(MousePoint: TPoint): TPoint;
begin
  result.X := trunc(MousePoint.X / (pntScreen.ClientWidth / CurrentGame.Map.GetWidth));
  result.Y := trunc(MousePoint.Y / (pntScreen.ClientHeight / CurrentGame.Map.GetHeight));
end;

procedure TfrmMain.pntScreenMouseDown(Sender: TObject; Button: TMouseButton;
  Shift: TShiftState; X, Y: Integer);
begin
  CurrentGame.FillMap;
end;

procedure TfrmMain.pntScreenMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var
  p: TPoint;
begin
  try
    p := MouseToMap(Point(x, y));
    CurrentGame.Map.SetPoint(p.X, p.Y, TMapPointType.pLine);
    pntScreen.Repaint;
  except
  end;
end;

procedure TfrmMain.pntScreenPaint(Sender: TObject);
begin
  MapRenderer.DrawMap(CurrentGame.Map,
    (Sender as TPaintBox).Canvas,
    (Sender as TPaintBox).ClientWidth, (Sender as TPaintBox).ClientHeight);
end;

end.
