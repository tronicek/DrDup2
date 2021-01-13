@echo off
call ..\setEnv.bat

set SRC_DIR="/research/jEdit5.3.0/src"

java %OPTIONS% -jar %DRDUP2_JAR% jedit.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jedit.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-jedit-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
