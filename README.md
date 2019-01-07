# cowbird

Postgres rewritten in elixir.

# Questions you may have

## Should you use this in production?

Yeah, if you're feeling frisky.

## What is it? 

It is many things. This repository is set up to allow for the development of an
elixir codebase that is functionally indistingusible from the official postgres
implementation. Cowbird uses the postgres test suite to ensure that all features that
matter in postgres get covered. 

## Why is it? 

I think that there is more for me to learn plumbing the depths of a well
established codebase and ecosystem than there is for me to go through the
exercise of writing my own sql implementation and database from scratch. 

## Why postgres? 

Postgres is the most well used, well respected and well documented open source SQL database avaliable (do not @ me).

## Why elixir? 

I heard elixir was the hot new thing and wanted to try it out. I had tried to
write a version of cowbird in clojure previously and had a difficult time
getting anywhere all too interesting, especially when dealing with binary
protocols. I bought the rust book and started trying to reimplement cowbird in that
language but it got a bit overwhelming so I stopped. Elixir is my next try, so let's see what happens.

## What is a cowbird? 

In nature, a cowbird is a bird that lays eggs in the nests of other birds.
Beyond the act of creating the egg and putting it in another nest, the noble
cowbird has no role in raising or parenting its offspring. Likewise, in
software, a cowbird is a codebase that uses another codebase's test suite and
documentation instead of its own. Writing good documentation and good tests is
actual work that I would have to be compensated for doing. A cowbird takes the
resources and established nature of a mature software project and allows me to
focus on the act of creation and learning instead of the sobering
responsbilities of explaining what I am doing and maintaining the codebase as
people use it out in the wild. Lifetimes of work years have been put into other
databases and I would rather use their ghosts than make my own.

## How can I contribute? 

In the traditional way, you mostly cannot contribute. Pull requests may be
accepted, but they are not strictly encouraged either. While this effort might
produce a drop in replacement for postgres, that's not the point. The point is
for me personally to learn more about a particular database and a particular
language. So the best way for you to contribute to this effort, would be to pick
a different database than postgres or a different language than elixir and make
a new cowbird and see what you end up learning. I've found this exercise to be
very helpful for me personally and I would encourage you to explore something
indepedent enough that you are out on your own for a bit.

## How to get this working 

```
git clone git@github.com:zmaril/cowbird.git
cd cowbird
git submodule update --init --recursive
cd resources/postgres
./configure
make
cd ../..
mix regress #custom mix task that builds the escripts executables and then runs the postgres regression test suite
```


## What are some interesting parts to look at?

* The parser for postgres' binary protocol. 
* The parser for postgres' sql implementation. 
* Some notes I've written up.
