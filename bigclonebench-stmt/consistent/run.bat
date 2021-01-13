@echo off
set SRC_DIR="/research/BigCloneEval/BigCloneEval/ijadataset/bcb_reduced"
set DRDUP_JAR="/research/projects/DrDup2/target/DrDup2-1.0-jar-with-dependencies.jar"
set TOOL_JAR="/research/projects/EvalTool/target/EvalTool-1.0-jar-with-dependencies.jar"
set CHECKER_JAR="/research/projects/CloneChecker/target/CloneChecker-1.0-jar-with-dependencies.jar"
set OPTIONS=-ea -Xmx14G

java %OPTIONS% -jar %DRDUP_JAR% bigclonebench.properties
java %OPTIONS% -cp %TOOL_JAR% drdup.Separator drdup-bigclonebench.xml

java %OPTIONS% -cp %TOOL_JAR% drdup.Sourcer %SRC_DIR% drdup-bigclonebench-separated.xml
java %OPTIONS% -jar %CHECKER_JAR% checker.properties
