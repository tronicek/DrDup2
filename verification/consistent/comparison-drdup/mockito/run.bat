@echo off
call ..\..\setEnv.bat

set SRC_DIR="/research/mockito/mockito-2.2.0/src/main"

java %OPTIONS% -jar %DRDUP_JAR% drdup-mockito-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-mockito-3.xml

java %OPTIONS% -jar %DRDUP_JAR% drdup-mockito-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-mockito-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-mockito-3.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-mockito-3.xml

java %OPTIONS% -jar %DRDUP2_JAR% drdup2-mockito-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup2-mockito-5.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-mockito-5-separated.xml drdup2-mockito-3-separated.xml drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup2-mockito-5-separated.xml drdup-mockito-3-separated.xml drdup2-drdup.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-drdup2.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-drdup.xml
