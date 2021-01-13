@echo off
call ..\setEnv.bat

set NICAD3="mockito-blind-minsize-3.xml"
set NICAD3d="mockito-blind-minsize-3-dir.xml"
set NICAD8="mockito-blind-minsize-8.xml"
set NICAD8d="mockito-blind-minsize-8-dir.xml"
set NICAD_SRC_DIR="/home/ubuntu/mockito/mockito-2.2.0/src/main/"
set SRC_DIR="/research/mockito/mockito-2.2.0/src/main"

java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD3%
java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD8%

java %OPTIONS% -jar %DRDUP2_JAR% mockito-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-mockito-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% mockito-8.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-mockito-8.xml

java -cp %TOOL_JAR% drdup.Diff %NICAD8d% drdup-mockito-3-separated.xml nicad-blind-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Diff drdup-mockito-8-separated.xml %NICAD3d% drdup-8-nicad-blind-3.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-blind-8-drdup-3.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-8-nicad-blind-3.xml

java -jar %CHECKER_JAR% checker.properties
