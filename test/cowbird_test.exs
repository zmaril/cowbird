defmodule CowbirdTest do
  use ExUnit.Case
  doctest Cowbird

  setup_all do
    test_dir = "resources/postgres/src/test/regress/"
    {:ok, cwd} = File.cwd()
    pg_regress = cwd <> "/" <> test_dir <> "pg_regress"
    {cmd_output, exit_code} = System.cmd(pg_regress,
      #These settings were holdovers from cowbird.clj, unsure what they all mean anymore, oh well.
      ["--inputdir=.",
       "--temp-instance=tmp_check",
       "--bindir=./bin",
       "--dlpath=.",
       "--schedule=./cowbird_schedule",
       "--outputdir=./test/output",
       "--host=localhost",
       "--use-existing"],
      env: [{"PATH", System.get_env("PATH") <> ":" <> cwd <> "/bin"}],
      stderr_to_stdout: true
    )
    {:ok, exit_code: exit_code, cmd_output: cmd_output}
    IO.puts(cmd_output)
  end

  test "Exit code good", state do
    assert state[:exit_code] == 0
  end

  test "Initdb worked", state do
    refute String.contains?(state[:cmd_output],"pg_regress: initdb failed")
  end
end
