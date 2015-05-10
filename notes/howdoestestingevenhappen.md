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

1544: [This seems to run the tests. Make sure to run make check beforehand so that pg_regress exists in the proper place. Also, the command must be run from a certain directory. You are smart though and can figure it out.](https://github.com/zmaril/cowbird/blob/0a5f351af7f643e1818be91b186d89cf18e97670/notes/makecheckoutput.txt#L1544).


What happens during the tests:

The tests use the scripts placed in the postgres `bin` during installation. The
first command used is
`initdb`. [These can be faked by the proper bash scripts I suppose](https://github.com/zmaril/cowbird/commit/8ea462ad5912ac60ae53b431179db279ff5c86cd),
jumping over to Clojure and making the data dir with the default
postgresql.conf. Up next is the big bad `postgresql` executable itself. This is
not as easy to fake though. Postgres uses Unix Domain Sockets to do Inter
Process Communication. Java doesn't support Unix Domain Sockets because, get
this, they don't work outside Unix, meaning they aren't portable, meaning they
aren't to be used in the JVM. Since I don't need the bourgeois luxury of
portability, I can use some native code to make this happen.

There is some precedence for this in
[monsanto/nreplds](https://github.com/monsanto/nreplds), so we're just gonna,
uh, use all that. Easy install junixsocket:
```
wget http://junixsocket.googlecode.com/files/junixsocket-1.3-bin.tar.bz2
tar xvf junixsocket-1.3-bin.tar.bz2
cd junixsocket-1.3
mvn install:install-file -Dfile=dist/junixsocket-1.3.jar -DartifactId=junixsocket -Dversion=1.3 -DgroupId=org.newsclub -Dpackaging=jar
sudo mkdir -p /opt/newsclub
sudo cp -r lib-native /opt/newsclub
```

