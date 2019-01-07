defmodule Cowbird.CLI do
  alias Cowbird.Initdb

  def main(args) do
    IO.puts("Loading up CLI main")
    IO.inspect(args)
    [mode | rest] = args
    case mode do
      "initdb" -> Initdb.main(rest)
    end
  end
end
