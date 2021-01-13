@echo off
call ..\setEnv.bat

set SRC_DIR="/research/OpenJDK/nashorn/src"

java %OPTIONS% -jar %DRDUP2_JAR% nashorn.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-nashorn.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-nashorn-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
