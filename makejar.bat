start /w cmd /c ant
set "scriptdir=%~dp0"
if not "%scriptdir:~-1%"=="\" set "scriptdir=%scriptdir%\"
cd "%scriptdir%dist"
start /w cmd /c ant
cd ..