@echo off
call ..\setEnv.bat

set NICAD5="h2-consistent-minsize-5.xml"
set NICAD5d="h2-consistent-minsize-5-dir.xml"
set NICAD10="h2-consistent-minsize-10.xml"
set NICAD10d="h2-consistent-minsize-10-dir.xml"
set NICAD_SRC_DIR="/home/ubuntu/h2/src/main/"
set SRC_DIR="/research/h2/h2database-version-1.4.196/h2/src/main"

java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD5%
java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD10%

java %OPTIONS% -jar %DRDUP2_JAR% h2-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-h2-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% h2-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-h2-10.xml

java -cp %TOOL_JAR% drdup.Diff %NICAD10d% drdup-h2-5-separated.xml nicad-consistent-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Diff drdup-h2-10-separated.xml %NICAD5d% drdup-10-nicad-consistent-5.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-consistent-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-10-nicad-consistent-5.xml

java -jar %CHECKER_JAR% checker.properties
