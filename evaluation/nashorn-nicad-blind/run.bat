@echo off
call ..\setEnv.bat

set NICAD5="nashorn-blind-minsize-5.xml"
set NICAD5d="nashorn-blind-minsize-5-dir.xml"
set NICAD10="nashorn-blind-minsize-10.xml"
set NICAD10d="nashorn-blind-minsize-10-dir.xml"
set NICAD_SRC_DIR="/home/ubuntu/nashorn/src/"
set SRC_DIR="/research/OpenJDK/nashorn/src"

java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD5%
java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD10%

java %OPTIONS% -jar %DRDUP2_JAR% nashorn-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-nashorn-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% nashorn-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-nashorn-10.xml

java -cp %TOOL_JAR% drdup.Diff %NICAD10d% drdup-nashorn-5-separated.xml nicad-blind-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Diff drdup-nashorn-10-separated.xml %NICAD5d% drdup-10-nicad-blind-5.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-blind-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-10-nicad-blind-5.xml

java -jar %CHECKER_JAR% checker.properties
