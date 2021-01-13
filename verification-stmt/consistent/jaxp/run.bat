@echo off
call ..\setEnv.bat

set SRC_DIR="/research/OpenJDK/jaxp/src"

java %OPTIONS% -jar %DRDUP2_JAR% jaxp.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jaxp.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-jaxp-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
