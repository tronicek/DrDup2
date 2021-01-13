@echo off
call ..\..\setEnv.bat

set SRC_DIR="/research/OpenJDK/nashorn/src"

java %OPTIONS% -jar %DRDUP_JAR% drdup-nashorn-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-nashorn-3.xml

java %OPTIONS% -jar %DRDUP_JAR% drdup-nashorn-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-nashorn-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-nashorn-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-nashorn-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-nashorn-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-nashorn-5.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-nashorn-5-separated.xml drdup2-nashorn-3-separated.xml drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-nashorn-5-separated.xml drdup-nashorn-3-separated.xml drdup2-drdup.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-drdup.xml
