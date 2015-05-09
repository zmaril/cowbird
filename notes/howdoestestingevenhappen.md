Understanding what the makefile for Postgres does line by line for regression
testing using `remake -n -s check`:

3: [First command cleans up and prepares the directory where all the files will
go](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L2)

4-1518: [Next couple of lines builds
Postgres](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L5-L1518)

1518-1532: This is a tricky part. [These lines make the C code that is used for
testing](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L1520-L1532). If
you use remake on these, you get a similiar output as the first remake command,
because [the regress makefile ensures that the project has actually been made
before doing
anything](https://github.com/postgres/postgres/blob/9a0884176fdfa51551d6a3b26fa0e1b216c3e4c2/src/test/regress/GNUmakefile#L17).

1535: [Apparently there is something called SPI](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L1535), which stands for [Server Proramming Interface](http://www.postgresql.org/docs/devel/static/spi.html). The more you know!

1544: [This seems to run the tests, but it cannot be run by itself sadly](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L1544).


