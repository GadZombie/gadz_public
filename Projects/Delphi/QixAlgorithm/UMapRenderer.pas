unit UMapRenderer;

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
  System.Classes,
  Vcl.Graphics,
  UMap;

type
  TMapRenderer = class
  public
    procedure DrawMap(Map: TMap; Canvas: TCanvas; CWidth, CHeight: integer);

  end;

implementation

{ TMapRenderer }

procedure TMapRenderer.DrawMap(Map: TMap; Canvas: TCanvas; CWidth, CHeight: integer);
var
  x, y: integer;
  width, height: double;
begin
  Canvas.Brush.Color := $00000000;
  Canvas.FillRect(Rect(0, 0, CWidth, CHeight));

  width := CWidth / Map.GetWidth;
  height := CHeight / Map.GetHeight;
  for y := 0 to Map.GetHeight - 1 do
    for x := 0 to Map.GetWidth - 1 do
    begin
      case Map.GetPoint(x, y) of
        TMapPointType.pEmpty:
          Canvas.Brush.Color := $00002040;
        TMapPointType.pLine:
          Canvas.Brush.Color := $00df5030;
        TMapPointType.pFilled:
          Canvas.Brush.Color := $00509845;
      end;

      Canvas.FillRect(
        Rect(trunc(x * Width), trunc(y * height),
             trunc((x + 1) * Width - 1), trunc((y + 1) * height - 1)
            ));

      if Map.GetOccupied(x, y) then
      begin
        Canvas.Brush.Color := $0011efff;
        Canvas.Pen.Color := $0011efff;
        Canvas.Ellipse(
          trunc(x * Width), trunc(y * height),
             trunc((x + 1) * Width - 1), trunc((y + 1) * height - 1)
            );

      end;

    end;

end;

end.
