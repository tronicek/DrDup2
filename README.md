# DrDup2
DrDup2 is a novel clone detector based on the index of abstract syntax trees.
Please, see the following paper for details:

Zdenek Tronicek, <a href="https://doi.org/10.1016/j.infsof.2021.106805">Indexing source code and clone detection</a>,
Information and Software Technology, Volume 144, 2022, 106805, ISSN 0950-5849.

## Compilation
To compile the code, you need to install ```maven``` and run the following 
command (see also ```compile.bat```):
```
mvn clean compile assembly:single
```

## Running
The run and output of DrDup2 are controlled by a configuration file,
which can contain the following configuration
parameters:
- *level* specifies the granularity of the index; accepted values are "method" and "statement".
- *compressed* specifies whether the index is compressed or not; accepted values are "true" and "false".
- *persistent* specifies whether the index is built in main memory or on secondary storage; accepted values are "true" and "false".
- *minSize* specifies the minimum number of lines; for example, if *minSize* is 5, the code fragment must have at least 5 lines to be reported.
- *ignoreUnaryAtLiterals* specifies how the unary plus and minus are treated; accepted values are "true" and "false".
- *ignoreAnnotations* specifies whether annotations in code are taken into account; accepted values are "true" and "false".
- *treatNullAsLiteral* specifies how "null" is treated; accepted values are "true" and "false".
- *treatSuperThisAsIdentifier* specifies whether "super" and "this" are treated as identifiers; accepted values are "true" and "false".
- *treatVoidAsType* specifies how "void" is treated; accepted values are "true" and "false".
- *batchFileSize* specifies how many files are processed before the index in memory is merged with the persistent index.

Example:
- index = simplified
- level = method
- rename = blind
- compressed = true
- persistent = false
- sourceDir = /research/BigCloneBench
- minSize = 20
- outputFile = bigclonebench.xml

To run the clone detector with ```test.properties``` configuration file,
use the following command:
```
java -jar target/DrDup2-1.0-jar-with-dependencies.jar test.properties
```

See also examples in the ```evaluation``` folder.
