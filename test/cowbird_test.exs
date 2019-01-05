defmodule CowbirdTest do
  use ExUnit.Case
  doctest Cowbird

  test "The whole thing" do
    test_dir = "resources/postgres/src/test/regress/"
    {:ok, cwd} = File.cwd() 
    pg_regress = cwd <> "/" <> test_dir <> "pg_regress"
    System.cmd(pg_regress,
      #These settings were holdovers from cowbird.clj, unsure what they all mean anymore, oh well.
      ["--inputdir=.",
       "--temp-instance=./tmp_check",
       "--bindir=",
       "--dlpath=.",
       "--schedule=./cowbird_schedule",
       "--output-dir=./test/output"
       "--host=localhost",
       "--use-existing"],
      env: [{"PATH", System.get_env("PATH") <> ":" <> cwd <> "/bin"}]
    )
  end
end
