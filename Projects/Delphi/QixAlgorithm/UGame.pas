unit UGame;

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
  WinApi.Windows,
  UMap,
  UMapFiller;

type
  TGame = class
    Map: TMap;
    MapFiller: TMapFiller;

  public
    constructor Create;
    destructor Destroy; override;

    procedure New;
    procedure Finish;

    procedure FillMap;
  end;

implementation

{ TGame }

constructor TGame.Create;
begin
  inherited;
  MapFiller := TMapFiller.Create;
end;

destructor TGame.Destroy;
begin
  MapFiller.Free;
  inherited;
end;

procedure TGame.FillMap;
begin
  MapFiller.FillMap(Map);
end;

procedure TGame.Finish;
begin
  FreeAndNil(Map);
end;

procedure TGame.New;
begin
  Map := TMap.Create;
  Map.Make(100, 80);

  //frame
  Map.DrawHLine(0, 0, Map.GetWidth - 1, TMapPointType.pLine);
  Map.DrawVLine(0, 0, Map.GetHeight - 1, TMapPointType.pLine);
  Map.DrawHLine(0, Map.GetHeight - 1, Map.GetWidth - 1, TMapPointType.pLine);
  Map.DrawVLine(Map.GetWidth - 1, 0, Map.GetHeight - 1, TMapPointType.pLine);
 {
  //test path 1
  Map.DrawVLine(20, Map.sy - 1, Map.sy - 10, TMapPointType.pLine);
  Map.DrawHLine(20, Map.sy - 10, 35, TMapPointType.pLine);
  Map.DrawVLine(35, Map.sy - 10, Map.sy - 17, TMapPointType.pLine);
  Map.DrawHLine(35, Map.sy - 17, 43, TMapPointType.pLine);
  Map.DrawVLine(43, Map.sy - 17, Map.sy - 1, TMapPointType.pLine);
                                  }
  Map.SetOccupied(15, 13, true);
  Map.SetOccupied(45, 33, true);

end;

end.
