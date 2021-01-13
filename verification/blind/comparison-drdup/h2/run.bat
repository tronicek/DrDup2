@echo off
call ..\..\setEnv.bat

set SRC_DIR="/research/h2/h2database-version-1.4.196/h2/src/main"

java %OPTIONS% -jar %DRDUP_JAR% drdup-h2-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-h2-3.xml

java %OPTIONS% -jar %DRDUP_JAR% drdup-h2-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-h2-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-h2-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-h2-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-h2-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-h2-5.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-h2-5-separated.xml drdup2-h2-3-separated.xml drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-h2-5-separated.xml drdup-h2-3-separated.xml drdup2-drdup.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-drdup.xml
