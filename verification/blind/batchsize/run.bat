@echo off
call ..\setEnv.bat

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-1.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-1.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-10.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-50.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-50.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-100.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-100.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-500.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-500.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-1000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-1000.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-full-5000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-full-5000.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-1.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-1.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-5.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-5.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-10.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-10.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-50.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-50.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-100.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-100.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-500.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-500.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-1000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-1000.xml

java %OPTIONS% -jar %DRDUP2_JAR% jedit-simplified-5000.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator jedit-simplified-5000.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-1-separated.xml     jedit-full-5-separated.xml      diff-full-1-5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-5-separated.xml     jedit-full-10-separated.xml     diff-full-5-10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-10-separated.xml    jedit-full-50-separated.xml     diff-full-10-50.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-50-separated.xml    jedit-full-100-separated.xml    diff-full-50-100.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-100-separated.xml   jedit-full-500-separated.xml    diff-full-100-500.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-500-separated.xml   jedit-full-1000-separated.xml   diff-full-500-1000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-1000-separated.xml  jedit-full-5000-separated.xml   diff-full-1000-5000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-full-5000-separated.xml  jedit-full-1-separated.xml      diff-full-5000-1.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-1-separated.xml     jedit-simplified-5-separated.xml      diff-simplified-1-5.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-5-separated.xml     jedit-simplified-10-separated.xml     diff-simplified-5-10.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-10-separated.xml    jedit-simplified-50-separated.xml     diff-simplified-10-50.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-50-separated.xml    jedit-simplified-100-separated.xml    diff-simplified-50-100.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-100-separated.xml   jedit-simplified-500-separated.xml    diff-simplified-100-500.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-500-separated.xml   jedit-simplified-1000-separated.xml   diff-simplified-500-1000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-1000-separated.xml  jedit-simplified-5000-separated.xml   diff-simplified-1000-5000.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff jedit-simplified-5000-separated.xml  jedit-simplified-1-separated.xml      diff-simplified-5000-1.xml

del data\*.bin
