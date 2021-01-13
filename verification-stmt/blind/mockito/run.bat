@echo off
call ..\setEnv.bat

set SRC_DIR="/research/mockito/mockito-2.2.0/src/main"

java %OPTIONS% -jar %DRDUP2_JAR% mockito.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-mockito.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-mockito-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
