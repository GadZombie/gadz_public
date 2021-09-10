program QixAlg;

uses
  Vcl.Forms,
  Main in 'Main.pas' {frmMain},
  UMap in 'UMap.pas',
  UMapFiller in 'UMapFiller.pas',
  UGame in 'UGame.pas',
  UGameFactory in 'UGameFactory.pas',
  UMapRenderer in 'UMapRenderer.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  Application.CreateForm(TfrmMain, frmMain);
  CurrentGame := TGameFactory.CreateGame;
  Application.Run;
end.
