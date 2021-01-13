@echo off
call ..\setEnv.bat

set SRC_DIR="/research/h2/h2database-version-1.4.196/h2/src/main"

java %OPTIONS% -jar %DRDUP2_JAR% h2.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-h2.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-h2-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
