List of bugs/errors I've found in Postgres so far:

1. Typo in error string when initdb fails. [The place that the log is saved to here](https://github.com/postgres/postgres/blob/master/src/test/regress/pg_regress.c#L2217) is `temp_instance`. [The reported location is `outputdir`, which is not the same](https://github.com/postgres/postgres/blob/master/src/test/regress/pg_regress.c#L2217). 
