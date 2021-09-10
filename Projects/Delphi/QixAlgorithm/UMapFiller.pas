unit UMapFiller;

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
 * http://classic-games.pl, http://gad.art.pl, gad@gad.art.pl
 *}

uses
  System.SysUtils,
  System.Classes,
  WinApi.Windows,
  UMap;

type

  TMapFiller = class
  strict private
    MapRef: TMap;
    WorkMap: TMap;

    FoundOccupied: boolean;
    CurrentId: integer;

    procedure Finish;
    function FindStart: TPoint;
    procedure Start(px, py: integer);
    procedure FillPoint(px, py: integer);
    procedure CopyFill;
    procedure CopyMapToWorkMap;

  public
    procedure FillMap(Map: TMap);
  end;

implementation

{ TMapFiller }

procedure TMapFiller.FillMap(Map: TMap);
var
  point: TPoint;
begin
  MapRef := Map;

  WorkMap := TMap.Create;
  CopyMapToWorkMap;
  CurrentId := 0;

  repeat
    point := FindStart;
    if point.X >= 0 then
    begin
      inc(CurrentId);
      Start(point.X, point.Y);
      CopyFill;
    end;
  until point.X < 0;
end;

procedure TMapFiller.Finish;
begin
  FreeAndNil(WorkMap);
  MapRef := nil;
end;

function TMapFiller.FindStart: TPoint;
var
  x, y: integer;
begin
  for y := 0 to WorkMap.GetHeight - 1 do
    for x := 0 to WorkMap.GetWidth - 1 do
      if WorkMap.GetPoint(x, y) = TMapPointType.pEmpty then
      begin
        result := Point(x, y);
        exit;
      end;

  result := Point(-1, -1);
end;

procedure TMapFiller.Start(px, py: integer);
begin
  FoundOccupied := false;
  FillPoint(px, py);
end;

procedure TMapFiller.FillPoint(px, py: integer);
begin
  WorkMap.SetPoint(px, py, TMapPointType.pFilled);
  WorkMap.SetId(px, py, CurrentId);
  if MapRef.GetOccupied(px, py) then
    FoundOccupied := true;
  if (px > 0) and (WorkMap.GetPoint(px - 1, py) = TMapPointType.pEmpty) then
    FillPoint(px - 1, py);
  if (px < WorkMap.GetWidth - 1) and (WorkMap.GetPoint(px + 1, py) = TMapPointType.pEmpty) then
    FillPoint(px + 1, py);
  if (py > 0) and (WorkMap.GetPoint(px, py - 1) = TMapPointType.pEmpty) then
    FillPoint(px, py - 1);
  if (py < WorkMap.GetHeight - 1) and (WorkMap.GetPoint(px, py + 1) = TMapPointType.pEmpty) then
    FillPoint(px, py + 1);
end;

procedure TMapFiller.CopyFill;
var
  x, y: integer;
begin
  if FoundOccupied then
    exit;

  for y := 0 to WorkMap.GetHeight - 1 do
  begin
    for x := 0 to WorkMap.GetWidth - 1 do
    begin
      if (MapRef.GetPoint(x, y) = TMapPointType.pEmpty) then
      begin
        if (WorkMap.GetPoint(x, y) = TMapPointType.pFilled) and
            (WorkMap.GetId(x, y) = CurrentId) then
          MapRef.SetPoint(x, y, TMapPointType.pFilled);

{        if not FoundOccupied then
        begin
          if (WorkMap.GetPoint(x, y) = TMapPointType.pFilled) then
            MapRef.SetPoint(x, y, TMapPointType.pFilled)
        end
        else
        begin
          if (WorkMap.GetPoint(x, y) = TMapPointType.pEmpty) then
            MapRef.SetPoint(x, y, TMapPointType.pFilled)
        end;}
      end;
    end;
  end;
end;

procedure TMapFiller.CopyMapToWorkMap;
var
  x, y: integer;
begin
  WorkMap.Make(MapRef.GetWidth, MapRef.GetHeight);
  for y := 0 to WorkMap.GetHeight - 1 do
    for x := 0 to WorkMap.GetWidth - 1 do
      WorkMap.SetPoint(x, y, MapRef.GetPoint(x, y));
end;


end.
