@echo off
call ..\setEnv.bat

set NICAD5="jaxp-blind-minsize-5.xml"
set NICAD5d="jaxp-blind-minsize-5-dir.xml"
set NICAD10="jaxp-blind-minsize-10.xml"
set NICAD10d="jaxp-blind-minsize-10-dir.xml"
set NICAD_SRC_DIR="/home/ubuntu/jaxp/src/"
set SRC_DIR="/research/OpenJDK/jaxp/src"

java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD5%
java -cp %TOOL_JAR% drdup.ChangeDir %NICAD_SRC_DIR% %NICAD10%

java %OPTIONS% -jar %DRDUP2_JAR% jaxp-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jaxp-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% jaxp-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jaxp-10.xml

java -cp %TOOL_JAR% drdup.Diff %NICAD10d% drdup-jaxp-5-separated.xml nicad-blind-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Diff drdup-jaxp-10-separated.xml %NICAD5d% drdup-10-nicad-blind-5.xml

java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-blind-10-drdup-5.xml
java -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-10-nicad-blind-5.xml

java -jar %CHECKER_JAR% checker.properties
