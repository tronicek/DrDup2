@echo off
call ..\..\setEnv.bat

set SRC_DIR="/research/OpenJDK/jaxp/src"

java %OPTIONS% -jar %DRDUP_JAR% drdup-jaxp-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jaxp-3.xml

java %OPTIONS% -jar %DRDUP_JAR% drdup-jaxp-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-jaxp-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-jaxp-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-jaxp-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-jaxp-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-jaxp-5.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-jaxp-5-separated.xml drdup2-jaxp-3-separated.xml drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-jaxp-5-separated.xml drdup-jaxp-3-separated.xml drdup2-drdup.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-drdup.xml
