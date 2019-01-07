defmodule Mix.Tasks.Regress do
  use Mix.Task

  @shortdoc "Compiles the escripts and then runs the regression test suite"
  def run(_) do
    Mix.Tasks.Escript.Build.run([])
    Mix.env(:test)
    Mix.Tasks.Test.run([])
  end
end
