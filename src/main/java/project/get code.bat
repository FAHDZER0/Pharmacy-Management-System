@echo off
setlocal enabledelayedexpansion

rem Get the directory where this bat file is located
set "baseDir=%~dp0"

rem List all subfolders in the same folder as the bat file
echo Available folders in %baseDir%:
set /a index=0
for /d %%i in ("%baseDir%*") do (
    set /a index+=1
    echo   !index!. %%~nxi
    set "folder[!index!]=%%i"
)

rem Prompt the user to choose a folder by index
set /p choice="Enter the folder index: "

rem Validate user input: if no folder corresponds to the index, exit.
if not defined folder[%choice%] (
    echo Invalid selection.
    pause
    exit /b
)

set "selectedFolder=!folder[%choice%]!"
echo You selected: %selectedFolder%

rem Extract the folder name (without path) from the selected folder
for %%F in ("%selectedFolder%") do set "folderName=%%~nF"

rem Set the output file name in the same folder as the bat file
set "outputFile=%baseDir%%folderName%.txt"
echo Output will be saved to: %outputFile%

rem Clear the output file
> "%outputFile%" echo.

rem Set the selected folder as the root directory for processing files
set "rootDir=%selectedFolder%"

rem Initialize file counter
set "fileCount=0"

rem accessing the lib folder in dir
rem set "rootDir=%rootDir%/lib"

echo Processing files in %rootDir% ...

rem Just Message For ChatGpt
echo. here is the updated code please analyze it and update your memory. >> !outputFile! 

rem Iterate recursively over all files in the selected folder and its subdirectories
for /r "%rootDir%" %%f in (*) do (
    set /a fileCount+=1

    rem Get the file's directory and remove the root directory part for clarity
    set "dirName=%%~dpf"
    set "dirName=!dirName:%rootDir%=!"
    if "!dirName!"=="" set "dirName=."

    rem Get the file name with extension
    set "fileName=%%~nxf"

    rem Write file info and content to the output file
    >> "%outputFile%" echo.
    >> "%outputFile%" echo File: !dirName!\!fileName!
    type "%%f" >> "%outputFile%"
    >> "%outputFile%" echo.

    echo Processed: !dirName!\!fileName!
)

echo Total number of files processed: !fileCount!
pause
endlocal