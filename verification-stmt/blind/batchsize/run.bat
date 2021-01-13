@echo off
call ..\setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% jedit-1.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-1.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-10.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-50.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-50.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-100.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-100.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-500.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-500.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-1000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-1000.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-5000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-5000.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-1-separated.xml     jedit-5-separated.xml     diff-1-5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-5-separated.xml     jedit-10-separated.xml    diff-5-10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-10-separated.xml    jedit-50-separated.xml    diff-10-50.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-50-separated.xml    jedit-100-separated.xml   diff-50-100.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-100-separated.xml   jedit-500-separated.xml   diff-100-500.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-500-separated.xml   jedit-1000-separated.xml  diff-500-1000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-1000-separated.xml  jedit-5000-separated.xml  diff-1000-5000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-5000-separated.xml  jedit-1-separated.xml     diff-5000-1.xml

del data\*.bin
