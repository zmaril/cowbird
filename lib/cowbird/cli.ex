defmodule Cowbird.CLI do
  alias Cowbird.Initdb

  def main(args) do
    IO.puts(args)
    case List.first(args) do
      "initdb" -> Cowbird.Initdb.main(args)
    end
  end
end
