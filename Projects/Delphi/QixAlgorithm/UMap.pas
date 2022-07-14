unit UMap;

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
  System.SysUtils,
  System.Classes,
  System.Generics.Collections,
  WinApi.Windows;

type
  TMapPointType = (pEmpty, pLine, pFilled);

  TMapPoint = class
  public
    Value: TMapPointType;
    Occupied: boolean;
    id: integer;

    constructor Create;
  end;

  TMapRow = TObjectList<TMapPoint>;
  TMapMatrix = TObjectList<TMapRow>;

  TMap = class
  private
    Width, Height: integer;
    points: TMapMatrix;

    procedure CheckPosition(px, py: integer);
  public
    constructor Create;
    destructor Destroy; override;
    procedure Make(sx, sy: integer);
    function GetPoint(px, py: integer): TMapPointType;
    procedure SetPoint(px, py: integer; Value: TMapPointType);
    function GetOccupied(px, py: integer): boolean;
    procedure SetOccupied(px, py: integer; Value: boolean);
    function GetId(px, py: integer): integer;
    procedure SetId(px, py: integer; Value: integer);
    procedure DrawHLine(x1, y, x2: integer; Value: TMapPointType);
    procedure DrawVLine(x, y1, y2: integer; Value: TMapPointType);

    function GetWidth: integer;
    function GetHeight: integer;
  end;

implementation

procedure Swap(var v1, v2: integer);
var
  tmp: integer;
begin
  tmp := v1;
  v1 := v2;
  v2 := tmp;
end;

{ TMapPoint }

constructor TMapPoint.Create;
begin
  inherited;
  Value := TMapPointType.pEmpty;
  Occupied := false;
  id := 0;
end;

{ TMap }

procedure TMap.CheckPosition(px, py: integer);
begin
  if (px < 0) or (py < 0) or (px >= Self.Width) or (py >= Self.Height)  then
    raise Exception.CreateFmt('Map point out of range [%dx%d]', [px, py]);
end;

constructor TMap.Create;
begin
  inherited;
  points := TMapMatrix.Create(true);
end;

destructor TMap.Destroy;
begin
  points.Free;
  inherited;
end;

procedure TMap.DrawHLine(x1, y, x2: integer; Value: TMapPointType);
var
  x: integer;
begin
  if x1 > x2 then
    Swap(x1, x2);
  for x := x1 to x2 do
    SetPoint(x, y, Value);
end;

procedure TMap.DrawVLine(x, y1, y2: integer; Value: TMapPointType);
var
  y: integer;
begin
  if y1 > y2 then
    Swap(y1, y2);
  for y := y1 to y2 do
    SetPoint(x, y, Value);
end;

procedure TMap.Make(sx, sy: integer);
var
  x, y: integer;
  MapRow: TMapRow;
begin
  self.Width := sx;
  self.Height := sy;

  points.Clear;
  for y := 0 to sy - 1 do
  begin
    MapRow := TMapRow.Create(true);
    for x := 0 to sx - 1 do
      MapRow.Add(TMapPoint.Create);
    points.Add(MapRow);
  end;

end;

procedure TMap.SetId(px, py, Value: integer);
begin
  CheckPosition(px, py);
  points[py][px].id := Value;
end;

procedure TMap.SetOccupied(px, py: integer; Value: boolean);
begin
  CheckPosition(px, py);
  points[py][px].Occupied := Value;
end;

procedure TMap.SetPoint(px, py: integer; Value: TMapPointType);
begin
  CheckPosition(px, py);
  points[py][px].Value := Value;
end;

function TMap.GetHeight: integer;
begin
  result := Height;
end;

function TMap.GetId(px, py: integer): integer;
begin
  CheckPosition(px, py);
  result := points[py][px].id;
end;

function TMap.GetOccupied(px, py: integer): boolean;
begin
  CheckPosition(px, py);
  result := points[py][px].Occupied;
end;

function TMap.GetPoint(px, py: integer): TMapPointType;
begin
  CheckPosition(px, py);
  result := points[py][px].Value;
end;

function TMap.GetWidth: integer;
begin
  result := Height;
end;

end.
