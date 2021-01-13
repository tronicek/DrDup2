@echo off
set SRC_DIR="/research/BigCloneEval/BigCloneEval/ijadataset/bcb_reduced"
set DRDUP_JAR="/research/projects/DrDup2/target/DrDup2-1.0-jar-with-dependencies.jar"
set TOOL_JAR="/research/projects/EvalTool/target/EvalTool-1.0-jar-with-dependencies.jar"
set OPTIONS=-ea -Xmx14G

java %OPTIONS% -jar %DRDUP_JAR% bigclonebench.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-bigclonebench.xml

set BIGCLONEBENCH_SRC_DIR="/home/ubuntu/BigCloneBench/bcb_reduced/"
java -cp %TOOL_JAR% drdup.ChangeDir %BIGCLONEBENCH_SRC_DIR% bigclonebench-consistent-minsize-6.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Diff drdup-bigclonebench-separated.xml bigclonebench-consistent-minsize-6-dir.xml drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Diff bigclonebench-consistent-minsize-6-dir.xml drdup-bigclonebench-separated.xml nicad-drdup2.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% nicad-drdup2.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Counter drdup2-nicad.xml
java %OPTIONS% -cp %TOOL_JAR% drdup.Counter nicad-drdup2.xml

java %OPTIONS% -jar %CHECKER_JAR% drdup2-nicad-checker.properties
java %OPTIONS% -jar %CHECKER_JAR% nicad-drdup2-checker.properties
