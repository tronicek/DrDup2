@echo off
call ..\setEnv.bat

set NICAD3="jedit-consistent-minsize-3.xml"
set NICAD3d="jedit-consistent-minsize-3-dir.xml"
set NICAD8="jedit-consistent-minsize-8.xml"
set NICAD8d="jedit-consistent-minsize-8-dir.xml"
set NICAD_SRC_DIR="/home/ubuntu/jEdit/jEdit5.3.0/"
set SRC_DIR="/research/jEdit5.3.0/src"

java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD3%
java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD8%

java %OPTIONS% -jar %DRDUP2_JAR% jedit-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-8.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit-8.xml

java -cp %TOOL_JAR% drdup.Diff %NICAD8d% drdup-jedit-3-separated.xml nicad-consistent-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Diff drdup-jedit-8-separated.xml %NICAD3d% drdup-8-nicad-consistent-3.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-consistent-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-8-nicad-consistent-3.xml

java -jar %CHECKER_JAR% checker.properties
